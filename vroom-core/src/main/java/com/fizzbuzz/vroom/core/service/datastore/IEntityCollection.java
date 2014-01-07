package com.fizzbuzz.vroom.core.service.datastore;

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

import com.fizzbuzz.vroom.core.domain.KeyedObject;

import java.util.List;

/**
 * Interface for "persist" classes that manage collections of domain objects
 */
public interface IEntityCollection<
        KO extends KeyedObject> {

    /**
     * Returns a set of KeyedObjects corresponding to the entities in an entity collection
     *
     * @return the elements in the collection
     */
    public List<KO> getElements();

    /**
     * Creates an entity in an entity collection corresponding to a keyed object.  Note: the provided keyed object
     * should not have an assigned key at invocation time (its value should be -1); however, on successful return,
     * the key will be assigned.
     *
     * @param keyedObject
     */
    public KO addElement(final KO keyedObject);

    /**
     * Deletes all entities in a collection
     */
    public void deleteAll();

    /**
     * Deletes entities in a collection corresponding to a a list of keyed objects
     *
     * @param keyedObjects the domain objects to delete
     */
    public void delete(final List<KO> keyedObjects);
}
