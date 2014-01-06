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

import com.fizzbuzz.vroom.core.api.resource.KeyedObjectCollectionResource;
import com.fizzbuzz.vroom.core.api.resource.VroomResource;
import com.fizzbuzz.vroom.core.domain.DomainCollection;
import com.fizzbuzz.vroom.sample.webservice.api.application.MediaTypes;
import com.fizzbuzz.vroom.sample.webservice.api.application.Uris;
import com.fizzbuzz.vroom.sample.webservice.api.dto_converter.UsersConverter;
import com.fizzbuzz.vroom.sample.webservice.biz.UsersBiz;
import com.fizzbuzz.vroom.sample.webservice.domain.User;
import com.fizzbuzz.vroom.sample.webservice.domain.Users;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersResource
        extends KeyedObjectCollectionResource<Users, User> {

    static final String PARAM_EMAIL = "email";
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    static public void register(List<ConverterHelper> converterHelpers) {
        VroomResource.registerResource(UsersResource.class, Uris.USERS);

        converterHelpers.add(new UsersConverter());
    }

    @Override
    @Get(MediaTypes.UsersMediaTypes.JSON_V1_0 + "|json")
    public DomainCollection<User> getResource() {
        Users result = new Users();

        Map<UsersBiz.UserConstraint, Object> constraints = new HashMap<>();

        String email = getStringParamValue(PARAM_EMAIL);
        if (email != null)
            constraints.put(UsersBiz.UserConstraint.EMAIL_EQUALS, email);

        if (constraints.isEmpty())
            result.addAll(super.getResource());
        else
            result.addAll(((UsersBiz) getCollectionBiz()).getFilteredElements(constraints));

        return result;
    }

    @Override
    @Post(MediaTypes.UserMediaTypes.JSON_V1_0 + "|json" + ":"
            + MediaTypes.UserMediaTypes.JSON_V1_0 + "|json")
    public User postResource(final User User) {
        return super.postResource(User);
    }

    @Override
    protected void doInit() {
        super.doInit(Users.class, new UsersBiz(), UserResource.class);
    }
}
