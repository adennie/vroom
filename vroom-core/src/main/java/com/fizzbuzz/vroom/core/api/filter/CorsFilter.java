package com.fizzbuzz.vroom.core.api.filter;

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

import com.fizzbuzz.vroom.core.api.service.CorsService;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.header.MethodWriter;
import org.restlet.routing.Filter;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CorsFilter extends Filter {
    public enum CorsResponseHeader {
        ALLOW_ORIGIN("Access-Control-Allow-Origin"),
        ALLOW_CREDENTIALS("Access-Control-Allow-Credentials"),
        EXPOSE_HEADERS("Access-Control-Expose-Headers"),
        MAX_AGE("Access-Control-Max-Age"),
        ALLOW_METHODS("Access-Control-Allow-Methods"),
        ALLOW_HEADERS("Access-Control-Allow-Headers");
        private final String name;

        CorsResponseHeader(final String name) {
            this.name = name;
        }
    }

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);
    private CorsService corsService;


    /**
     * Constructor.
     *
     * @param context The parent context.
     */
    public CorsFilter(Context context) {
        super(context);
    }

    @Override
    protected void afterHandle(final Request request, final Response response) {
        super.afterHandle(request, response);

        Series<Header> requestHeaders = (Series<Header>)
                request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);

        String origin = requestHeaders.getFirstValue("Origin", true);


        if (origin != null) {
            // handle pre-flight OPTIONS requests
            Method requestedMethod = getRequestedMethod(requestHeaders);
            List<String> requestedHeaders = getRequestedHeaders(requestHeaders);

            if (request.getMethod() == Method.OPTIONS && requestedMethod != null) {
                handlePreflightRequest(request, response, origin, requestedMethod, requestedHeaders);
            } else
                handleNormalRequest(request, response, origin);
        }
    }

    protected boolean supportsCredentials(final Request request, final Response response) {
        // this default implementation uses the CorsService's configuration to determine whether credentials
        // are supported.  Override this method to implement more specific logic, such as determining this on a
        // per-resource basis.
        return getCorsService().getAllowCredentials();
    }

    protected long getMaxAge(final Request request, final Response response) {
        // this default implementation uses the CorsService's configuration for the max age.  Override this
        // method to implement more specific logic, such as determining this on a per-resource basis
        return getCorsService().getMaxAge();
    }

    protected Set<Method> getAllowedMethods(final Request request, final Response response) {
        // this implementation returns the set of methods implemented on the resource.  Override this method
        // to implement more specific logic.
        Set<Method> result = response.getAllowedMethods();
        return result;
    }

    protected CorsService getCorsService() {
        if (corsService == null)
            corsService = getApplication().getServices().get(CorsService.class);
        return corsService;
    }

    protected boolean allowAllOrigins(final Request request, final Response response) {
        return getAllowedOrigins(request, response).contains("*");
    }

    protected List<String> getAllowedOrigins(final Request request, final Response response) {
        return getCorsService().getAllowedOrigins();
    }

    protected void handlePreflightRequest(final Request request, final Response response,
                                          final String origin, final Method requestedMethod,
                                          final List<String> requestedHeaders) {
        // We already now that the origin header was provided; that was checked in afterHandle
        // See http://www.w3.org/TR/cors section 6.2, step 1

        // Check whether the origin is in the allowed list of origins (this is a case-sensitive compare,
        // See http://www.w3.org/TR/cors section 6.2, step 2
        if (originIsAllowed(request, response, origin)) {

            // We already know that the requested method is non-null; that was already checked in afterHandle
            // See http://www.w3.org/TR/cors section 6.2, step 3

            // We have the list of requested headers.  See http://www.w3.org/TR/cors section 6.2, step 4

            // Check that the requested method is allowed
            // See http://www.w3.org/TR/cors section 6.2, step 5
            if (methodIsAllowed(request, response, requestedMethod)) {

                // Check that all of the requested headers are allowed. Note: this is a case-insensitive compare
                // See http://www.w3.org/TR/cors section 6.2, step 6
                if (allRequestedHeadersAreAllowed(request, response, requestedHeaders)) {

                    // If the resource supports credentials, add an Access-Control-Allow-Origin header for the origin,
                    // and a Access-Control-Allow-Credentials header.  Otherwise, add an Access-Control-Allow-Origin
                    // header, with either the value of the Origin header or the string "*" as value.
                    // see http://www.w3.org/TR/cors section 6.2, step 7
                    if (supportsCredentials(request, response)) {
                        addResponseHeader(response, CorsResponseHeader.ALLOW_ORIGIN, origin);
                        addResponseHeader(response, CorsResponseHeader.ALLOW_CREDENTIALS, "true");
                    } else {
                        addResponseHeader(response, CorsResponseHeader.ALLOW_ORIGIN,
                                allowAllOrigins(request, response) ? "*" : origin);
                    }

                    // Optionally add a single Access-Control-Max-Age header with as value the amount of seconds the
                    // user agent is allowed to cache the result of the request.
                    // See http://www.w3.org/TR/cors section 6.2, step 8
                    if (getMaxAge(request, response) > 0) {
                        addResponseHeader(response, CorsResponseHeader.MAX_AGE,
                                Long.toString(getCorsService().getMaxAge()));
                    }

                    // Set the Access-Control-Allow-Methods header to reflect the methods allowed.
                    // See http://www.w3.org/TR/cors section 6.2, step 9
                    addResponseHeader(response, CorsResponseHeader.ALLOW_METHODS,
                            MethodWriter.write(getAllowedMethods(request, response)));

                    // Add Access-Control-Allow-Headers headers for each requested header
                    // See http://www.w3.org/TR/cors section 6.2, step 10
                    for (String allowedHeader : getAllowedHeaders(request, response))
                        addResponseHeader(response, CorsResponseHeader.ALLOW_HEADERS, allowedHeader);
                }
            }
        }
    }

    protected List<String> getAllowedHeaders(final Request request, final Response response) {
        // This default implementation uses the allowed headers configured on the CorsService.  Override this
        //  method to implement more specific logic, such as determining the allowed headers on a per-resource basis.
        List<String> result = getCorsService().getAllowedHeaders();
        return result;
    }

    protected void handleNormalRequest(final Request request, final Response response, final String origin) {
        // We already now that the origin header was provided; that was checked in afterHandle.
        // See http://www.w3.org/TR/cors section 6.1, step 1

        // Check that the origin is allowed.
        // See http://www.w3.org/TR/cors section 6.1, step 2
        if (originIsAllowed(request, response, origin)) {
            // If the resource supports credentials, add an Access-Control-Allow-Origin header for the origin,
            // and a Access-Control-Allow-Credentials header.  Otherwise, add an Access-Control-Allow-Origin
            // header, with either the value of the Origin header or the string "*" as value.
            // see http://www.w3.org/TR/cors section 6.1, step 3
            if (supportsCredentials(request, response)) {
                addResponseHeader(response, CorsResponseHeader.ALLOW_ORIGIN, origin);
                addResponseHeader(response, CorsResponseHeader.ALLOW_CREDENTIALS, "true");
            } else {
                addResponseHeader(response, CorsResponseHeader.ALLOW_ORIGIN,
                        allowAllOrigins(request, response) ? "*" : origin);
            }

            // Indicate which response headers should be exposed
            // See http://www.w3.org/TR/cors section 6.1, step 4
            List<String> exposedHeaders = getExposedHeaders(request, response);
            if (!exposedHeaders.isEmpty()) {
                addResponseHeader(response, CorsResponseHeader.EXPOSE_HEADERS, Joiner.on(",").join(exposedHeaders));
            }

        }
    }

    protected List<String> getExposedHeaders(final Request request, final Response response) {
        // This default implementation uses the exposed headers configured on the CorsService.  Override this
        // method to implement more specific logic, such as determining the exposed headers on a per-resource basis.
        return getCorsService().getExposedHeaders();
    }

    protected void addResponseHeader(final Response response, final CorsResponseHeader corsHeader, final String value) {
        addCustomResponseHeader(response, corsHeader.name, value);
    }

    private Method getRequestedMethod(final Series<Header> requestHeaders) {
        String requestedMethod = requestHeaders.getFirstValue("Access-Control-Request-Method", true);
        return requestedMethod == null ? null : new Method(requestedMethod);
    }

    private List<String> getRequestedHeaders(final Series<Header> requestHeaders) {
        // Get the list of requested headers
        List<String> requestedHeaders = new ArrayList<>();
        String accessControlRequestHeaders = requestHeaders.getFirstValue("Access-Control-Request-Headers", true);
        if (accessControlRequestHeaders != null)
            requestedHeaders.addAll(Lists.newArrayList(Splitter.on(",").trimResults().split
                    (accessControlRequestHeaders)));
        return requestedHeaders;
    }

    private boolean originIsAllowed(final Request request, final Response response, final String origin) {
        List<String> allowedOrigins = getAllowedOrigins(request, response);
        boolean result = allowAllOrigins(request, response) || allowedOrigins.contains(origin);
        if (!result)
            mLogger.debug("origin not allowed: {}.  Allowed origins: {}", origin, allowedOrigins);
        return result;
    }

    private boolean methodIsAllowed(final Request request, final Response response, final Method requestedMethod) {
        Set<Method> allowedMethods = getAllowedMethods(request, response);
        boolean result = allowedMethods.contains(requestedMethod);
        if (!result)
            mLogger.debug("method not allowed: {}.  Allowed methods: {}", requestedMethod, allowedMethods);
        return result;
    }

    private boolean allRequestedHeadersAreAllowed(final Request request, final Response response,
                                                  final List<String> requestedHeaders) {
        List<String> allowedHeaders = getAllowedHeaders(request, response);
        boolean allAreAllowed = true;
        for (String header : requestedHeaders) {
            boolean thisOneIsAllowed = false;
            for (String allowedHeader : allowedHeaders) {
                if (header.equalsIgnoreCase(allowedHeader)) {
                    thisOneIsAllowed = true;
                    break;
                }
            }
            if (!thisOneIsAllowed) {
                mLogger.debug("header not allowed: {}.  Allowed headers: {}", header, allowedHeaders);
                allAreAllowed = false;
                break;
            }
        }
        return allAreAllowed;
    }

    private void addCustomResponseHeader(final Response response, final String header, final String value) {
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
