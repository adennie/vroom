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

import java.util.List;

/**
 * Interface for entity collection classes
 */
public interface IEntityCollection<
        EO extends IEntityObject> {

    /**
     * Returns a set of domain objects corresponding to the entities in an entity collection
     *
     * @return the elements in the collection
     */
    public List<EO> getElements();

    /**
     * Creates an entity in an entity collection corresponding to a domain object.  Note: the provided domain object
     * should not have an assigned key at invocation time (its value should be null); however, on successful return,
     * the key will be assigned.
     *
     * @param domainObject
     */
    public void addElement(final EO domainObject);

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

    /*
      Load and Saves all entities to migrate entity completely to any schema change
     */
    public void rewriteAll();
}
