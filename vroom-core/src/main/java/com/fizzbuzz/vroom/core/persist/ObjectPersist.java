package com.fizzbuzz.vroom.core.persist;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

import com.fizzbuzz.vroom.core.domain.DomainObject;

/**
 * Interface for "persist" objects that manage individual DomainObjects.
 */
public interface ObjectPersist<DO extends DomainObject> {

    /**
     * Returns the DomainObject having the provided ID
     *
     * @param id the DomainObject's ID
     * @return the DomainObject corresponding to the provided ID
     */
    public DO get(final long id);

    /**
     * Updates a DomainObject's state.  Often this will require merging the state of the provided DomainObject with the
     * existing persisted state of that object and then persisting that new, merged state.
     *
     * @param modelObject the new state for the DomainObject
     */
    public void update(final DO modelObject);

    /**
     * Deletes the DomainObject having the provided ID
     *
     * @param id the DomainObject's ID
     */
    public void delete(final long id);

}