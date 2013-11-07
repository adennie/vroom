package com.fizzbuzz.vroom.core.persist;

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

import com.fizzbuzz.vroom.core.domain.IdObject;

import java.util.List;

/**
 * Interface for "persist" classes that manage collections of domain objects
 */
public interface EntityCollection<
        DO extends IdObject> {

    /**
     * Retrieves the domain objects in a collection
     *
     * @return the domain objects in the collection
     */
    public List<DO> getDomainElements();

    /**
     * Add a domain object to a persistent collection.  Note: the provided PersistentObject should not have an
     * assigned ID field at invocation time (its value should be -1); however, on successful return,
     * the ID field will be assigned.
     *
     * @param domainObject
     */
    public DO addElement(final DO domainObject);

    /**
     * Deletes all domain objects in a collection
     */
    public void deleteAll();

    /**
     * Deletes a list of domain objects in a collection
     *
     * @param domainObjects the domain objects to delete
     */
    public void delete(List<DO> domainObjects);

}
