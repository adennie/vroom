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

/**
 * Interface for "persist" objects that manage individual DomainObjects.
 */
public interface IEntity<KO extends KeyedObject> {

    /**
     * Creates a new entity corresponding to a keyed domain object
     *
     * @param keyedObject the domain object
     */
    public void create(KO keyedObject);

    /**
     * Returns the KeyedObject having the provided key
     *
     * @param key the KeyedObject's key
     * @return the KeyedObject corresponding to the provided key
     */
    public KO get(final Long key);

    /**
     * Updates an entity's state.  Often this will require merging the state of the provided KeyedObject with the
     * existing persisted state of that object and then saving that new, merged state.
     *
     * @param keyedObject the new state for the KeyedObject
     */
    public void update(final KO keyedObject);

    /**
     * Deletes the PersistentObject having the provided ID
     *
     * @param id the PersistentObject's ID
     */
    public void delete(final Long id);

}