package com.fizzbuzz.vroom.core.persist.datastore.dao;

/*
 * Copyright (c) 2013 Fitivity Inc.
 */

import com.fizzbuzz.vroom.core.domain.KeyedObject;

import java.util.Date;

public abstract class TimeStampedDao<KO extends KeyedObject> extends BaseDao<KO> {
    private Date createdDate;
    private Date modifiedDate;

    // no-arg constructor needed by Objectify
    public TimeStampedDao() {
    }

    protected TimeStampedDao(final Long id) {
        super(id);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {

        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
