package com.fizzbuzz.vroom.core.persist;

import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.googlecode.objectify.annotation.Id;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public abstract class BaseDao<DO extends DomainObject> {
    @Id private Long mId;

    // no-arg constructor used by Objectify
    protected BaseDao() {
    }

    protected BaseDao(final long id) {
        mId = id;
    }

    /**
     * Converts this DAO to an object of its corresponding domain object type
     *
     * @return
     */
    public abstract DO toDomainObject();

    public long getId() {
        return mId;
    }

    public void clearId() {
        mId = null;
    }
}
