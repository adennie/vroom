package com.andydennie.vroom.sample.webservice.service.datastore.dao;
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

import com.andydennie.vroom.core.domain.LongKey;
import com.andydennie.vroom.core.service.datastore.TimeStampedDao;
import com.andydennie.vroom.sample.webservice.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UserDao extends TimeStampedDao<User> {
    @Index private String firstName;
    @Index private String lastName;
    @Index private String email;
    private Ref<ImageDao> profileImage;
    private Ref<PlaceDao> home;

    // no-arg constructor needed by Objectify
    public UserDao() {
    }

    /**
     * This constructor is invoked via reflection by Entity and EntityCollection.
     *
     * @param user a User domain object
     */
    public UserDao(final User user) {
        super(user);
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        profileImage = Ref.create(Key.create(ImageDao.class, user.getProfileImageKey().get()));
        home = Ref.create(Key.create(PlaceDao.class, user.getHomeKey().get()));
    }

    @Override
    public User toDomainObject() {
        return new User(new LongKey(getId()), firstName, lastName, email, new LongKey(profileImage.getKey().getId()),
                new LongKey(home.getKey().getId()));
    }

}
