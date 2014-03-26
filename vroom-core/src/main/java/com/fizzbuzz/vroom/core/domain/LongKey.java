package com.fizzbuzz.vroom.core.domain;

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

public class LongKey extends KeyType<Long> {
    public LongKey() {
        this((Long)null);
    }
    public LongKey(final Long key) {
        super(key);
    }

    public LongKey(final String keyAsString) {
            super(Long.parseLong(keyAsString));
    }
}
