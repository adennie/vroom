package com.fizzbuzz.vroom.core.api.filter;

/*
 * Copyright (c) 2014 Fizz Buzz LLC
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

public class LogHeadersFilter extends Filter {

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);


    /**
     * Constructor.
     *
     * @param context The parent context.
     */
    public LogHeadersFilter(Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        Series<Header> requestHeaders = (Series<Header>)
                request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);

        for (Header header : requestHeaders) {
            mLogger.debug(header.toString());
        }

        return super.beforeHandle(request, response);
    }
}
