package com.fizzbuzz.vroom.core.persist;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

import com.fizzbuzz.vroom.core.domain.DomainObject;

import java.util.List;

/**
 * Interface for "persist" classes that manage collections of domain objects
 */
public interface CollectionPersist<
        DO extends DomainObject> {

    /**
     * Retrieves the domain objects in a collection
     *
     * @return the domain objects in the collection
     */
    public List<DO> getDomainElements();

    /**
     * Add a domain object to a persistent collection.  Note: the provided DomainObject should not have an
     * assigned ID field at invocation time (its value should be -1); however, on successful return,
     * the ID field will be assigned.
     *
     * @param domainObject
     */
    public DO add(final DO domainObject);

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
