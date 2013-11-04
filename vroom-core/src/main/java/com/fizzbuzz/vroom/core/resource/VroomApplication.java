package com.fizzbuzz.vroom.core.resource;

/*
 * Copyright (c) 2013 Fizz Buzz LLC
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

import com.fizzbuzz.vroom.core.persist.OfyService;
import com.fizzbuzz.vroom.core.persist.PersistManager;
import com.google.appengine.api.labs.modules.ModulesService;
import com.google.appengine.api.labs.modules.ModulesServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.routing.Router;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
    private boolean mCorsEnabled = false;
    private List<String> mAllowedOrigins = new ArrayList<>();

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

    public List<String> getAllowedOrigins() {
        return mAllowedOrigins;
    }

    public void setAllowedOrigins(final List<String> allowedOrigins) {
        mAllowedOrigins = allowedOrigins;
    }

    public boolean isCorsEnabled() {
        return mCorsEnabled;
    }

    public void setCorsEnabled(final boolean corsEnabled) {
        mCorsEnabled = corsEnabled;
    }

    @Override
    public void handle(final Request request, final Response response) {
        mLogger.info("VroomApplication.handle: request received - {}", request);
        super.handle(request, response);

        if (mCorsEnabled) {
            Series<Header> requestHeaders = (Series<Header>)
                    request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
            String origin = requestHeaders.getFirstValue("Origin", true);
            if (getAllowedOrigins().contains(origin)) {
                addCustomResponseHeader(response, "Access-Control-Allow-Origin", origin);
            }
            addCustomResponseHeader(response, "Access-Control-Allow-Headers", "Content-Type");
            addCustomResponseHeader(response, "Access-Control-Allow-Headers", "authCode");
            addCustomResponseHeader(response, "Access-Control-Allow-Headers", "origin, x-requested-with, content-type");
        }

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

    protected void addCustomResponseHeader(final Response response, final String header, final String value) {
        Series<Header> responseHeaders = (Series<Header>)
                response.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
        if (responseHeaders == null) {
            responseHeaders = new Series(Header.class);
            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                    responseHeaders);
        }
        responseHeaders.add(new Header(header, value));
    }

    /**
     * Returns the application's OfyService object.  Subclasses of VroomApplication must implement this method.
     *
     * @return the OfyService object
     */
    protected abstract OfyService getOfyService();
}

