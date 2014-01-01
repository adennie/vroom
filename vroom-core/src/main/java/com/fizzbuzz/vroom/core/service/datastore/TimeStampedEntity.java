package com.fizzbuzz.vroom.core.service.datastore;

/*
 * Copyright (c) 2013 Fizz Buzz LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.domain.LongKey;

import java.util.Date;

/**
 * Abstract base class for "entity" objects that correspond to keyed domain objects, with built-in support for
 * maintaining creation and modification date/times.
 */
public abstract class TimeStampedEntity<KO extends KeyedObject<LongKey>, DAO extends TimeStampedDao<KO>> extends
        Entity<KO, DAO> {

    protected TimeStampedEntity(final Class<KO> domainClass, final Class<DAO> daoClass) {
        super(domainClass, daoClass);
    }

    /**
     * Updates the stored entity for a KeyedObject.  The creation date/time from the previously stored entity is
     * reused and the modification date/time is updated to the current time.
     *
     * @param keyedObject
     */
    @Override
    public void update(final KO keyedObject) {
        DAO oldDao = getDao(keyedObject.getKey().get());
        DAO newDao = createDao(keyedObject);
        newDao.setCreatedDate(oldDao.getCreatedDate());
        newDao.setModifiedDate(new Date(System.currentTimeMillis()));
        saveDao(newDao);
    }

    /**
     * Creates a DAO that is initialized from the provided domain object, with its creation and modification
     * date/time set to now.
     *
     * @param keyedObject a keyed domain object
     * @return a DAO initialized from the domain object
     */
    @Override
    protected DAO createDao(final KO keyedObject) {
        DAO result = super.createDao(keyedObject);
        Date now = new Date(System.currentTimeMillis());
        result.setCreatedDate(now);
        result.setModifiedDate(now);
        return result;
    }
}