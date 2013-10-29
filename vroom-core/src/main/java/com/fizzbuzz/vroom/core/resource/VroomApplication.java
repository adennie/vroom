package com.fizzbuzz.vroom.core.resource;

import com.fizzbuzz.vroom.core.persist.OfyService;
import com.fizzbuzz.vroom.core.persist.PersistManager;
import com.google.appengine.api.labs.modules.ModulesService;
import com.google.appengine.api.labs.modules.ModulesServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public abstract class VroomApplication
        extends Application {

    public enum ExecutionContext {
        DEVELOPMENT,
        PRODUCTION
    }

    private static ExecutionContext mExecutionContext;

    static {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production)
            mExecutionContext = ExecutionContext.PRODUCTION;
        else
            mExecutionContext = ExecutionContext.DEVELOPMENT;
    }

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public static ExecutionContext getExecutionContext() {
        return mExecutionContext;
    }

    public static boolean inDevelopmentContext() {
        return mExecutionContext == ExecutionContext.DEVELOPMENT;
    }

    public static boolean inProductionContext() {
        return mExecutionContext == ExecutionContext.PRODUCTION;
    }

    public static String getServerUrl() {
        ModulesService modulesService = ModulesServiceFactory.getModulesService();
        return modulesService.getModuleHostname(modulesService.getCurrentModule(), modulesService.getCurrentVersion());
    }

    @Override
    public void handle(final Request request, final Response response) {
        mLogger.info("VroomApplication.handle: request received - {}", request);
        super.handle(request, response);
    }

    @Override
    public synchronized void start() throws Exception {
        super.start();

        PersistManager.registerOfyService(getOfyService());
    }

    protected void attach(Router router, String pathTemplate, java.lang.Class<? extends org.restlet.resource
            .ServerResource> target) {
        mLogger.debug("attaching path template {} to {}", pathTemplate, target);
        router.attach(pathTemplate, target);
    }

    /**
     * Returns the application's OfyService object.  Subclasses of VroomApplication must implement this method.
     *
     * @return the OfyService object
     */
    protected abstract OfyService getOfyService();
}

