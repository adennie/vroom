package com.andydennie.vroom.core.api.resource;

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

import com.andydennie.vroom.core.api.application.VroomApplication;
import com.andydennie.vroom.core.exception.ConflictException;
import com.andydennie.vroom.core.exception.InvalidResourceUriException;
import com.andydennie.vroom.core.exception.NotFoundException;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.resource.Options;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class VroomResource<R extends Object> extends ServerResource implements IVroomResource<R> {

    private List<CacheDirective> mEdgeCacheDirectives = new ArrayList<>();

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);
    private boolean mEdgeCachingEnabled = false;

    public boolean isEdgeCachingEnabled() {
        return mEdgeCachingEnabled;
    }

    public void enableEdgeCaching(final int maxAge) {
        // limit to 364 days.  See https://groups.google.com/d/msg/google-appengine/6xAV2Q5x8AU/QI26C0ofvhwJ
        if (maxAge > 60*60*24*364)
            throw new IllegalArgumentException("For Edge Caching, max-age should be no more than 364 days");

        mEdgeCachingEnabled = true;
        mEdgeCacheDirectives.clear();
        mEdgeCacheDirectives.add(CacheDirective.publicInfo());
        mEdgeCacheDirectives.add(CacheDirective.maxAge(maxAge));
    }

    public void disableEdgeCaching() {
        mEdgeCachingEnabled = false;
    }

    @Override
    public R getResource() {
        return null;
    }

    @Override
    public void putResource(final R object) {
    }

    @Override
    public void deleteResource() {
    }

    @Options
    public void doOptions() {
        // setting the Allow response header is done automatically by restlet as long as this @Options-annotated
        // method exists
    }

    public VroomApplication getApplication() {
        return (VroomApplication) super.getApplication();
    }

    // this default implementation just returns the path template.  If the template contains any tokens, the
    // subclass should override this method and perform the token substitution.
    public String getPath() {
        String pathTemplate = getPathTemplate();
        if (pathTemplate.contains("{"))
            throw new IllegalStateException("this resource's URI template contains tokens which must be substituted " +
                    "with values.");
        return pathTemplate;

    }

    protected void addEdgeCachingHeaders() {
        // See https://groups.google.com/d/msg/google-appengine/6xAV2Q5x8AU/QI26C0ofvhwJ
        getResponse().setCacheDirectives(mEdgeCacheDirectives);
        addPragmaResponseHeader("Public");
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

    protected String getPathTemplate() {
        return ResourceRegistry.getPathTemplate(this.getClass());
    }

    // this is really just here to provide access to the doInit method from the unit test
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
    }

    protected void addPragmaResponseHeader(final String value) {
        addResponseHeader(HeaderConstants.HEADER_PRAGMA, value);
    }

    protected void addResponseHeader(final String header, final String value) {

        Response response = getResponse();
        Series<Header> responseHeaders = (Series<Header>)
                response.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
        if (responseHeaders == null) {
            responseHeaders = new Series(Header.class);
            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                    responseHeaders);
        }
        responseHeaders.add(new Header(header, value));
    }
}
