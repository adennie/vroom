package com.fizzbuzz.vroom.core.api.resource;

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

import com.fizzbuzz.vroom.core.api.application.BaseApplication;
import com.fizzbuzz.vroom.core.exception.ConflictException;
import com.fizzbuzz.vroom.core.exception.InvalidResourceUriException;
import com.fizzbuzz.vroom.core.exception.NotFoundException;
import org.restlet.data.Status;
import org.restlet.resource.Options;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseResource extends ServerResource {
    private static Map<Class<? extends BaseResource>, String> mResourceClassToCanonicalUriPathTemplateMap = new
            HashMap<>();

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public static <R extends BaseResource> void registerResource(
            final Class<R> resourceClass, final String canonicalUriPathTemplate) {
        mResourceClassToCanonicalUriPathTemplateMap.put(resourceClass, canonicalUriPathTemplate);
    }

    public static <R extends BaseResource> String getCanonicalUriPathTemplate(Class<R> resourceClass) {
        return mResourceClassToCanonicalUriPathTemplateMap.get(resourceClass);
    }


    @Options
    public void doOptions() {
        // setting the Allow response header is done automatically by restlet as long as this @Options-annotated
        // method exists
    }

    public BaseApplication getApplication() {
        return (BaseApplication) super.getApplication();
    }

    protected void doCatch(final RuntimeException e) {
        Class<?> exceptionClass = e.getClass();
        if (IllegalArgumentException.class.isAssignableFrom(exceptionClass)
                || InvalidResourceUriException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 400
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage(), e);
        }
        if (NotFoundException.class.isAssignableFrom(exceptionClass)
                || IndexOutOfBoundsException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 404
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getMessage(), e);
        }
        if (UnsupportedOperationException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 405
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, e.getMessage(), e);
        }
        if (ConflictException.class.isAssignableFrom(exceptionClass)
                || IllegalStateException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 409
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_CONFLICT, e.getMessage(), e);
        }

        throw e;
    }

    protected long getLongTokenValue(final String token) {

        String s = getTokenValue(token);

        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "invalid " + token + " URL component: " + s);
        }
    }

    protected String getTokenValue(final String token) {
        String result = (String) getRequestAttributes().get(token);

        if (result == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "missing " + token + " URL component: " +
                    result);
        }

        return result;
    }


    protected Long getLongParamValue(final String paramName) {
        String paramAsString = null;
        Map<String, String> params = getQuery().getValuesMap();
        paramAsString = params.get(paramName);

        if (paramAsString == null)
            return null;
        return Long.parseLong(paramAsString);
    }

    protected String getStringParamValue(final String paramName) {
        Map<String, String> params = getQuery().getValuesMap();
        return params.get(paramName);

    }

    protected String getCanonicalUriPathTemplate() {
        return getCanonicalUriPathTemplate(this.getClass());
    }

    // this default implementation just returns the path template.  If the template contains any tokens, the
    // subclass should override this method and perform the token substitution.
    public String getCanonicalUriPath() {
        String pathTemplate = getCanonicalUriPathTemplate();
        if (pathTemplate.contains("{"))
            throw new IllegalStateException("this resource's URI template contains tokens which must be substituted " +
                    "with values.");
        return pathTemplate;

    }

    // this is really just here to provide access to the doInit method from the unit test
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
    }
}