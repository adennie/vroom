package com.fizzbuzz.vroom.sample.webservice.service.datastore;
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

import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.service.datastore.VroomDao;
import com.fizzbuzz.vroom.sample.webservice.domain.User.UserLogEntry;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;

import java.util.Date;

@Entity
public class UserLogEntryDao extends VroomDao<UserLogEntry> {
    UserLogEntry.UserEventType eventType;
    Date dateTime;
    private Ref<UserDao> user;

    @Override
    public void fromDomainObject(final UserLogEntry userLogEntry) {
        super.fromDomainObject(userLogEntry);
        if (userLogEntry.getUserKey() == null)
            throw new IllegalArgumentException("the provided UserLogEntry must have a non-null user key");

        user = Ref.create(Key.create(UserDao.class, userLogEntry.getUserKey().get()));
        dateTime = userLogEntry.getDateTime();
        eventType = userLogEntry.getEventType();
    }

    @Override
    public UserLogEntry toDomainObject() {
        LongKey userKey = new LongKey(user.getKey().getId());
        return new UserLogEntry(new LongKey(getId()), userKey, eventType, dateTime);
    }

}
