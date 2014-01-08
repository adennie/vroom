package com.andydennie.vroom.core.api.application;

/*
 * Copyright (c) 2014 Andy Dennie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.andydennie.vroom.core.service.datastore.OfyService;
import com.andydennie.vroom.core.service.datastore.OfyManager;
import com.andydennie.vroom.core.service.datastore.OfyService;
import com.google.appengine.api.labs.modules.ModulesService;
import com.google.appengine.api.labs.modules.ModulesServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class VroomApplication
        extends Application {

    public enum ExecutionContext {
        DEVELOPMENT,
        PRODUCTION
    }

    private static ExecutionContext mExecutionContext;
    private static String mRootUrl;
    private static String mServerUrl;
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    static {
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production)
            mExecutionContext = ExecutionContext.PRODUCTION;
        else
            mExecutionContext = ExecutionContext.DEVELOPMENT;
    }

    public static String getRootUrl() {
        return mRootUrl;
    }

    public static void registerRootUrl(final String rootUrl) {
        mRootUrl = rootUrl;
    }

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
        if (mServerUrl == null) {
            ModulesService modulesService = ModulesServiceFactory.getModulesService();
            mServerUrl = "http://" + modulesService.getModuleHostname(modulesService.getCurrentModule(),
                    modulesService.getCurrentVersion());
        }
        return mServerUrl;
    }

    @Override
    public void handle(final Request request, final Response response) {
        mLogger.info("request received: {}", request);
        mLogger.debug("request headers: {}", request.getAttributes());
        super.handle(request, response);
        mLogger.debug("response headers: {}", response.getAttributes());


    }

    @Override
    public synchronized void start() throws Exception {
        super.start();

        OfyManager.registerOfyService(getOfyService());
    }

    protected void attach(Router router, String pathTemplate, java.lang.Class<? extends org.restlet.resource
            .ServerResource> target) {
        mLogger.info("attaching path template {} to {}", pathTemplate, target);
        router.attach(pathTemplate, target);
    }

    /**
     * Returns the application's OfyService object.  Subclasses of VroomApplication must implement this method.
     *
     * @return the OfyService object
     */
    protected abstract OfyService getOfyService();
}

