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

import com.fizzbuzz.vroom.core.api.resource.KeyedResource;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.sample.webservice.api.application.MediaTypes;
import com.fizzbuzz.vroom.sample.webservice.biz.UserBiz;
import com.fizzbuzz.vroom.sample.webservice.domain.User;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

public class UserResource
        extends KeyedResource<LongKey, UserBiz, User> {

    @Override
    @Get(MediaTypes.UserMediaTypes.JSON_V1_0 + "|json")
    public User getResource() {
        User result = null;
        try {
            result = super.getResource();
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Override
    @Put(MediaTypes.UserMediaTypes.JSON_V1_0 + "|json")
    public void putResource(final User user) {
        try {
            super.putResource(user);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit(new UserBiz());
    }
}
