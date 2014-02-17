package com.fizzbuzz.vroom.core.service.datastore;

/*
 * Copyright (c) 2014 Fitivity Inc.
 */

import com.fizzbuzz.vroom.core.domain.IEntityObject;

import java.util.Date;

/**
 * Abstract base class for "entity" objects that correspond to keyed domain objects, with built-in support for
 * maintaining creation and modification date/times.
 */
public abstract class TimeStampedEntity<EO extends IEntityObject, DAO extends TimeStampedDao<EO>> extends
        VroomEntity<EO, DAO> {

    protected TimeStampedEntity(final Class<EO> domainClass, final Class<DAO> daoClass) {
        super(domainClass, daoClass);
    }

    /**
     * Updates the DAO's modification date/time to the current date/time.
     * @param domainObject the domain object containing the new state
     * @return the created DAO
     */
    @Override
    protected DAO createDao(final EO domainObject) {
        DAO dao = super.createDao(domainObject);
        Date now = new Date(System.currentTimeMillis());
        dao.setCreatedDate(now);
        return dao;
    }

    /**
     * Updates the DAO's modification date/time to the current date/time.
     * @param dao the DAO to update
     * @param domainObject the domain object containing the new state
     */
    @Override
    protected void updateDao(final DAO dao, final EO domainObject) {
        super.updateDao(dao, domainObject);
        Date now = new Date(System.currentTimeMillis());
        dao.setModifiedDate(now);
    }
}