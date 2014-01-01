package com.fizzbuzz.vroom.sample.webservice.domain;

/*
 * Copyright (c) 2013 Fizz Buzz LLC
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

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.domain.LongKey;

public class User extends KeyedObject<LongKey> {
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private LongKey mImageKey;

    public User(final Long key, final String firstName, final String lastName, final String email, final LongKey imageKey) {
        super(new LongKey(key));
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mImageKey = imageKey;
    }
}
