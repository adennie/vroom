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

import com.fizzbuzz.vroom.core.api.resource.KeyedCollectionResource;
import com.fizzbuzz.vroom.sample.webservice.api.application.MediaTypes;
import com.fizzbuzz.vroom.sample.webservice.biz.UsersBiz;
import com.fizzbuzz.vroom.sample.webservice.domain.User;
import com.fizzbuzz.vroom.sample.webservice.domain.Users;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UsersResource
        extends KeyedCollectionResource<Users, User, UsersBiz> {

    static final String PARAM_EMAIL = "email";
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    @Override
    @Get(MediaTypes.UsersMediaTypes.JSON_V1_0 + "|json")
    public Users getResource() {
        Users result = new Users();
        try {
            Map<UsersBiz.UserConstraint, Object> constraints = new HashMap<>();

            String email = getStringParamValue(PARAM_EMAIL);
            if (email != null)
                constraints.put(UsersBiz.UserConstraint.EMAIL_EQUALS, email);

            if (constraints.isEmpty())
                result.addAll(super.getResource());
            else
                result.addAll(((UsersBiz) getCollectionBiz()).getFilteredElements(constraints));
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Override
    @Post(MediaTypes.UserMediaTypes.JSON_V1_0 + "|json" + ":"
            + MediaTypes.UserMediaTypes.JSON_V1_0 + "|json")
    public User postResource(final User User) {
        User result = null;
        try {
            return super.postResource(User);
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Override
    @Delete
    public void deleteResource() {
        try {
            super.deleteResource();
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Override
    protected void doInit() {
        super.doInit(Users.class, new UsersBiz(), UserResource.class);
    }
}
