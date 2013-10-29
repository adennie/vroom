package com.fizzbuzz.vroom.core.domain;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class ParentedDomainObject extends DomainObject {
    private long mParentId;

    public ParentedDomainObject(final long id, final long parentId) {
        super(id);
        mParentId = parentId;
    }

    public long getParentId() {
        return mParentId;
    }
}
