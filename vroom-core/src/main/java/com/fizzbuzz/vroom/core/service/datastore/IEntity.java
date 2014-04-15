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
import java.util.List;

/**
 * Interface for classes that manage datastore entities
 */
public interface IEntity<EO extends IEntityObject> {

    /**
     * Creates a new entity corresponding to a domain object
     *
     * @param domainObject the domain object
     */
    public void create(EO domainObject);

    /**
     * Creates new entities corresponding to a collection of domain objects
     *
     * @param domainObjects the domain objects
     */
    public void create(Collection<EO> domainObjects);


    /**
     * Returns the KeyedObject having the provided key
     *
     * @param key the entity object's key
     * @return the entity object corresponding to the provided key
     */
    public EO get(final Long key);

    /**
     * Returns a collection of domain objects corresponding to "all" entities (where "all" is defined by the
     * implementing subclass)
     *
     * @return the elements in the collection
     */
    public List<EO> getAll();

    /**
     * Returns a list of entity IDs corresponding to "all" entities (where "all" is defined by the
     * implementing subclass)
     *
     * @return
     */
    public List<Long> getAllIds();

        /**
         * Updates an entity's state.  Often this will require merging the state of the provided entity object with the
         * existing state of that object's corresponding entity and then saving that new, merged state.
         *
         * @param domainObject the new state for the entity object
         */
    public void update(final EO domainObject);

    /**
     * Updates the states of entities corresponding to a collection of domain objects.  Often this will require
     * merging the states of the provided domain objects with the existing states of those objects' corresponding
     * entities and then saving those new, merged states.
     *
     * @param domainObjects the domain objects
     */
    public void update(final Collection<EO> domainObjects);

    /**
     * Deletes the entity having the provided key
     *
     * @param key the domain object's key
     */
    public void delete(final Long key);

    /**
     * Deletes the entity corresponding to the provided domain object
     *
     * @param domainObject the domain object whose corresponding entity should be deleted
     */
    public void delete(final EO domainObject);

    /**
     * Deletes all entities (where "all" is defined by the implementing subclass)
     */
    public void deleteAll();

    /**
     * Deletes entities corresponding to those in a collection of domain objects
     *
     * @param domainObjects the domain objects to delete
     */
    public void delete(final Collection<EO> domainObjects);

    /**
     * Allocates a single ID for an entity which is not part of an entity group
     *
     * @return an allocated ID
     */
    public long allocateId();

    /**
     * Allocates a contiguous range of IDs for entities which are not part of an entity group
     *
     * @param num the number of keys to allocate.  Must be >= 1 and <= 1 billion.
     * @return a list of allocated IDs
     */
    public List<Long> allocateIds(int num);
}