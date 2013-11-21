package com.fizzbuzz.vroom.core.persist.datastore;

import com.fizzbuzz.vroom.core.persist.datastore.dao.BaseDao;
import com.googlecode.objectify.annotation.Entity;

/**
 * Copyright (c) 2013 Fitivity Inc.
 */

@Entity
public class TestDao extends BaseDao<TestKeyedObject> {
    @Override
    public TestKeyedObject toDomainObject() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
