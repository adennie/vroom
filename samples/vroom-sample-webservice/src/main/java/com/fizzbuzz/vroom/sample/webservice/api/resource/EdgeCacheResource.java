package com.fizzbuzz.vroom.sample.webservice.api.resource;

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

import com.fizzbuzz.vroom.core.api.resource.VroomResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

public class EdgeCacheResource
        extends VroomResource<String> {

    @Override
    @Get
    public String getResource() {
        String result = null;
        try {
            result = "Cache me if you can!";
            addEdgeCachingHeaders();
            throw new IllegalArgumentException();
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();

        // enable caching of GET responses in the Edge Cache.  
        enableEdgeCaching(60 * 60 * 24 * 364); // 364 days
    }
}
