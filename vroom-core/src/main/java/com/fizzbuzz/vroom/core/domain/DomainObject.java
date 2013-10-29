package com.fizzbuzz.vroom.core.domain;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class DomainObject {
    private long mId;

    public DomainObject(final long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(final long id) {
        mId = id;
    }

    public void validate() {
    }
}
