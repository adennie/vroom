package com.fizzbuzz.vroom.core.biz;

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

import com.fizzbuzz.vroom.core.domain.IDomainObject;

import java.util.List;

public interface ICollectionBiz<DO extends IDomainObject> {
    /**
     * Returns the list of domain objects within a collection
     * @return the list of domain objects
     */
    public List<DO> getElements();

    /**
     * Adds a domain object to the collection.
     *
     * @param domainObject
     */
    public void add(final DO domainObject);

    /**
     * Deletes all domain objects in the collection
     */
    public void deleteAll();

    /**
     * Deletes a list of domain objects
     * @param domainCollection
     */
    public void delete(List<DO> domainCollection);
}
