package com.andydennie.vroom.core.api.service;

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

import com.andydennie.vroom.core.api.filter.CorsFilter;
import org.restlet.Context;
import org.restlet.routing.Filter;
import org.restlet.service.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CorsService extends Service {
    private final List<String> allowedHeaders;
    private final List<String> allowedOrigins; // may be "*"
    private final List<String> exposedHeaders;
    private volatile boolean allowCredentials = false;
    private volatile long maxAge = 0;

    public CorsService() {
        this.allowedHeaders = new CopyOnWriteArrayList<>();
        this.allowedOrigins = new CopyOnWriteArrayList<>();
        this.exposedHeaders = new CopyOnWriteArrayList<>();
    }

    public boolean getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(final boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(final long maxAge) {
        this.maxAge = maxAge;

    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    @Override
    public Filter createInboundFilter(Context context) {
        return new CorsFilter(context);
    }
}
