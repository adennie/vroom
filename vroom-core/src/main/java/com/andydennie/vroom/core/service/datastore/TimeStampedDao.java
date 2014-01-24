package com.andydennie.vroom.core.service.datastore;

/*
 * Copyright (c) 2014 Fitivity Inc.
 */

import com.andydennie.vroom.core.domain.IEntityObject;

import java.util.Date;

public abstract class TimeStampedDao<KO extends IEntityObject> extends VroomDao<KO> {
    private Date createdDate;
    private Date modifiedDate;

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
