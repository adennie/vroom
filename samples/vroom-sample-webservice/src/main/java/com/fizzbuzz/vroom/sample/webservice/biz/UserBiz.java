package com.fizzbuzz.vroom.sample.webservice.biz;
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

import com.fizzbuzz.vroom.core.biz.FilterableEntityBiz;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.service.datastore.IDatastoreService;
import com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService;
import com.fizzbuzz.vroom.sample.webservice.domain.User;
import com.fizzbuzz.vroom.sample.webservice.domain.User.UserLogEntry;
import com.fizzbuzz.vroom.sample.webservice.service.datastore.UserEntity;
import com.fizzbuzz.vroom.sample.webservice.service.datastore.UserLogEntryEntity;

import java.util.Date;

public class UserBiz
        extends FilterableEntityBiz<User, UserEntity, UserBiz.UserConstraint> {
    public enum UserConstraint {
        EMAIL_EQUALS
    }

    public UserBiz() {
        super(new UserEntity());
    }

    @Override
    public void add(final User user) {
        // pre-allocate an ID for the user entity so we can use it in the UserLogEntry that references this
        // user, and then save both the user and the log entry together in a single idempotent transaction.
        user.setKey(getEntity().allocateKey());

        // preallocate an ID for the user log entry, too (if we don't preallocate the ID, more than one entity could
        // be created if the transaction is retried).
        final UserLogEntryEntity userLogEntryEntity = new UserLogEntryEntity();
        final LongKey userLogEntryKey = userLogEntryEntity.allocateKey();

        final UserLogEntry userCreatedEntry = new UserLogEntry(userLogEntryKey, user.getKey(),
                UserLogEntry.UserEventType.USER_CREATED, new Date(System.currentTimeMillis()));

        new VroomDatastoreService().doInTransaction(new IDatastoreService.Transactable<Void>() {
          @Override
          public Void run() {
            UserBiz.super.add(user);
            userLogEntryEntity.create(userCreatedEntry);
            return null;
          }
        });
    }
}
