package com.fizzbuzz.vroom.sample.webservice.api.resource;

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

import com.fizzbuzz.vroom.core.api.resource.KeyedObjectResource;
import com.fizzbuzz.vroom.sample.webservice.api.application.MediaTypes;
import com.fizzbuzz.vroom.sample.webservice.api.application.UriTokens;
import com.fizzbuzz.vroom.sample.webservice.api.application.Uris;
import com.fizzbuzz.vroom.sample.webservice.api.dto_converter.UserConverter;
import com.fizzbuzz.vroom.sample.webservice.biz.UserBiz;
import com.fizzbuzz.vroom.sample.webservice.domain.User;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import java.util.List;

public class UserResource
        extends KeyedObjectResource<UserBiz, User> {

    static public void register(List<ConverterHelper> converterHelpers) {
        KeyedObjectResource.registerResource(UserResource.class, Uris.USER_TEMPLATE);
        KeyedObjectResource.registerIdToken(UserResource.class, UriTokens.USER_ID);

        converterHelpers.add(new UserConverter());
    }

    @Get(MediaTypes.UserMediaTypes.JSON_V1_0 + "|json")
    public User getResource() {
        return super.getResource();
    }

    @Override
    @Put(MediaTypes.UserMediaTypes.JSON_V1_0 + "|json")
    public void putResource(final User user) {
        super.putResource(user);
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit(new UserBiz());
    }


}
