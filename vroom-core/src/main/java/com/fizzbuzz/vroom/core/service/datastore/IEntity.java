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

import java.util.List;

/**
 * Interface for classes that manage datastore entities
 */
public interface IEntity<EO extends IEntityObject> {

    /**
     * Creates a new entity corresponding to a entity object
     *
     * @param domainObject the domain object
     */
    public void create(EO domainObject);

    /**
     * Returns the KeyedObject having the provided key
     *
     * @param key the entity object's key
     * @return the entity object corresponding to the provided key
     */
    public EO get(final Long key);

    /**
     * Updates an entity's state.  Often this will require merging the state of the provided entity object with the
     * existing saved state of that object and then saving that new, merged state.
     *
     * @param domainObject the new state for the entity object
     */
    public void update(final EO domainObject);

    /**
     * Deletes the domain object having the provided key
     *
     * @param key the domain object's key
     */
    public void delete(final Long key);

    /**
     * Returns a set of domain objects corresponding to the entities in an entity collection
     *
     * @return the elements in the collection
     */
    public List<EO> getAll();

    /**
     * Deletes all entities in a collection
     */
    public void deleteAll();

    /**
     * Deletes entities in a collection corresponding to a list of domain objects
     *
     * @param domainObjects the domain objects to delete
     */
    public void delete(final List<EO> domainObjects);

    /**
     * Allocates a single ID for an entity which is not part of an entity group
     *
     * @return an allocated ID
     */
    public long allocateId();

    /**
     * Allocates a contiguous range of IDs for entities which are not part of an entity group
     * @param num the number of keys to allocate.  Must be >= 1 and <= 1 billion.
     *
     * @return a list of allocated IDs
     */
    public List<Long> allocateIds(int num);
}