package com.fizzbuzz.vroom.core.domain;

import java.util.List;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public interface DomainCollection<DO extends DomainObject> {
    /**
     * Returns a list of the domain objects in the collection
     * @return the domain objects in the collection
     */
    public List<DO> getElements();

    /**
     * Adds a domain object to the collection.
     *
     * @param domainObject
     */
    public DO add(final DO domainObject);

    /**
     * Deletes all domain objects in the collection
     */
    public void deleteAll();

    /**
     * Deletes a list of domain objects in the collection
     * @param domainObjects
     */
    public void delete(List<DO> domainObjects);
}
