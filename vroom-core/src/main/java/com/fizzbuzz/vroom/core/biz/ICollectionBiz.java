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

import java.util.Collection;

public interface ICollectionBiz<DO> {
    /**
     * Returns a collection of domain objects
     * @return the collection
     */
    public Collection<DO> getAll();

    /**
     * Adds a domain object to the collection.
     *
     * @param domainObject domain object to add
     */
    public void add(final DO domainObject);

    /**
     * Adds domain objects to the collection
     *
     * @param domainObjects domain objects to add
     */
    public void add(final Collection<DO> domainObjects);

    /**
     * Deletes all domain objects in the collection
     */
    public void deleteAll();

    /**
     * Deletes a collection of domain objects
     * @param domainCollection collection of domain objects to delete
     */
    public void delete(Collection<DO> domainCollection);
}
