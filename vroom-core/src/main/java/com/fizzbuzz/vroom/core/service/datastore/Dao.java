package com.fizzbuzz.vroom.core.service.datastore;

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
import com.googlecode.objectify.annotation.Id;

public abstract class Dao<KO extends KeyedObject> {
    @Id private Long id;

    // no-arg constructor used by Objectify
    protected Dao() {
    }

    protected Dao(final Long id) {
        this.id = id;
    }

    /**
     * Converts this DAO to an object of its corresponding domain object type
     *
     * @return the corresponding domain object
     */
    public abstract KO toDomainObject();

    public Long getId() {
        return id;
    }
}
