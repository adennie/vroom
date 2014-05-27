package com.fizzbuzz.vroom.core.service.datastore;

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

import com.fizzbuzz.vroom.core.domain.IEntityObject;
import com.fizzbuzz.vroom.core.domain.LongKey;

import java.util.Collection;

public interface IParentedEntity<EO extends IEntityObject> {
    /**
     * Deletes a parented entity
     *
     * @param parentKey the parent entity
     * @param childKey  the child entity
     */
    public void delete(final LongKey parentKey, final LongKey childKey);

    /**
     * Deletes parented entities corresponding to a collection of domain objects
     *
     * @param parentKey     the parent entity
     * @param domainObjects the domain objects whose corresponding entities should be deleted (must all have the same
     *                      parent entity identified by parentKey)
     */
    public void delete(final LongKey parentKey, final Collection<EO> domainObjects);

    /**
     * Delete all entities of the kind corresponding to EO which have the specified parent
     *
     * @param parentKey
     */
    public void deleteAll(LongKey parentKey);
}
