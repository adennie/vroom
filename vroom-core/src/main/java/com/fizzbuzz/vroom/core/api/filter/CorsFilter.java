package com.fizzbuzz.vroom.core.api.filter;

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

import com.fizzbuzz.vroom.core.api.service.CorsService;
import com.google.common.base.Joiner;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.header.MethodWriter;
import org.restlet.routing.Filter;
import org.restlet.util.Series;

import java.util.List;

public class CorsFilter extends Filter {


    /**
     * Constructor.
     *
     * @param context
     *            The parent context.
     */
    public CorsFilter(Context context) {
        super(context);
    }


    @Override
    protected void afterHandle(final Request request, final Response response) {
        super.afterHandle(request, response);

        CorsService corsService = getApplication().getServices().get(CorsService.class);

        Series<Header> requestHeaders = (Series<Header>)
                request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);


        // handle pre-flight OPTIONS requests
        if (request.getMethod() == Method.OPTIONS &&
                requestHeaders.getFirstValue("Access-Control-Request-Method", true) != null) {
            // this is a pre-flight request.  Set the Access-Control-Allow-Methods header to reflect
            // the methods allowed on this resource
            addCustomResponseHeader(response, "Access-Control-Allow-Methods",
                    MethodWriter.write(response.getAllowedMethods()));

            // if the client is requesting the use of request headers, include an Access-Control-Allow-Headers
            // response header for each requested header that is allowed
            String accessControlRequestHeader = requestHeaders.getFirstValue("Access-Control-Request-Header",
                    true);
            if (accessControlRequestHeader != null) {
                String[] requestedHeaders = accessControlRequestHeader.split(",");
                for (String header : requestedHeaders) {
                    if (corsService.getAllowedHeaders().contains(header)) {
                        addCustomResponseHeader(response, "Access-Control-Allow-Headers", header);
                    }
                }

            }
        } else {
            // this is a normal (not pre-flight) request
            // indicate which response headers should be exposed
            List<String> exposedHeaders = corsService.getExposedHeaders();
            if (!exposedHeaders.isEmpty()) {
                addCustomResponseHeader(response, "Access-Control-Expose-Headers",
                        Joiner.on(",").join(exposedHeaders));
            }
        }

        // remaining logic applies to both pre-flight and normal requests...

        // check the request's origin -- if it's in the list of allowed origins,
        // add an Access-Control-Allow-Origin response header for that origin.
        String origin = requestHeaders.getFirstValue("Origin", true);
        if (corsService.getAllowedOrigins().contains(origin)) {
            addCustomResponseHeader(response, "Access-Control-Allow-Origin", origin);
        }

        // if credentials (cookies, HTTP authentication, and client-side SSL certificates) are allowed,
        // include a Access-Control-Allow-Credentials response header.
        // Note: for pre-flight OPTIONS requests, this tells the client that credentials are allowed.  For
        // other requests, if the request contained credentials and this response header is not present, the
        // browser will not pass the response back to JavaScript.
        if (corsService.getAllowCredentials())
            addCustomResponseHeader(response, "Access-Control-Allow-Credentials", "true");
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
