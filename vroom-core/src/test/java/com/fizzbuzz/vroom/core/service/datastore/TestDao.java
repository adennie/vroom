package com.fizzbuzz.vroom.core.service.datastore;

import com.googlecode.objectify.annotation.Entity;

/**
 * Copyright (c) 2013 Fitivity Inc.
 */

@Entity
public class TestDao extends Dao<TestKeyedObject> {
    @Override
    public TestKeyedObject toDomainObject() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
