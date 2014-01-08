package com.andydennie.vroom.core.service.datastore;

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

import com.andydennie.vroom.core.domain.KeyedObject;
import com.andydennie.vroom.core.domain.LongKey;
import com.googlecode.objectify.annotation.Id;

public abstract class VroomDao<KO extends KeyedObject<LongKey>> {
    @Id private Long id;

    // no-arg constructor used by Objectify
    protected VroomDao() {
    }

    protected VroomDao(final KO keyedObject) {
        this.id = keyedObject.getKey().get();
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
