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

import com.andydennie.vroom.core.domain.IEntityObject;

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

}