package com.fizzbuzz.vroom.sample.webservice.service.datastore;
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

import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.service.datastore.VroomDao;
import com.fizzbuzz.vroom.extension.googlecloudstorage.service.datastore.GcsDao;
import com.fizzbuzz.vroom.sample.webservice.domain.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UserDao extends VroomDao<User> {
    @Index private String firstName;
    @Index private String lastName;
    @Index private String email;
    private Ref<GcsDao> profileImage;
    private Ref<PlaceDao> home;

    @Override
    public void fromDomainObject(final User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        if (user.getProfileImageKey() != null)
            profileImage = Ref.create(Key.create(GcsDao.class, user.getProfileImageKey().get()));
        if (user.getHomeKey() != null)
            home = Ref.create(Key.create(PlaceDao.class, user.getHomeKey().get()));
    }

    @Override
    public User toDomainObject() {
        LongKey profileImageKey = null;
        if (profileImage != null)
            profileImageKey = new LongKey(profileImage.getKey().getId());

        LongKey homeKey = null;
        if (home != null)
            homeKey = new LongKey(home.getKey().getId());
        return new User(new LongKey(getId()), firstName, lastName, email, profileImageKey, homeKey);
    }

}
