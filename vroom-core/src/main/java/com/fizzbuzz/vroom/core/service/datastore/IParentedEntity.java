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

import java.util.Collection;

public interface IParentedEntity<EO extends IEntityObject> {
    public void delete(final Long parentId, final Long id);

    /**
     * Deletes parented entities corresponding to a collection of domain objects
     *
     * @param parentId
     * @param domainObjects
     */
    public void delete(final Long parentId, final Collection<EO> domainObjects);

    /**
     * Delete all entities of the kind corresponding to EO which have the specified parent
     * @param parentId
     */
    public void deleteAll(Long parentId);
}
