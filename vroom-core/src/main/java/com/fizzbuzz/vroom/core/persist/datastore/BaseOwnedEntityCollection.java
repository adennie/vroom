package com.fizzbuzz.vroom.core.persist.datastore;

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.googlecode.objectify.Key;

import java.util.List;


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

/**
 * A base persist class for managing collections of entities which are logically "owned" by another entity.
 * The owned entity stores a reference to the key of its owner, allowing the {@link #getElements()} method to
 * constrain the results to those belonging to that particular owner.
 *
 * @param <OWNERKO>  the owner domain object type (a KeyedObject<Long>)
 * @param <OWNEDKO>  the owned domain object type (a KeyedObject<Long>)
 * @param <OWNERDAO> the owner DAO type
 * @param <OWNEDDAO> the owned DAO type
 */
public abstract class BaseOwnedEntityCollection<
        OWNERKO extends KeyedObject<LongKey>,
        OWNEDKO extends KeyedObject<LongKey>,
        OWNERDAO extends BaseDao<OWNERKO>,
        OWNEDDAO extends OwnedDao<OWNERDAO, OWNERKO, OWNEDKO>>
        extends BaseEntityCollection<OWNEDKO, OWNEDDAO> {

    private Class<OWNERDAO> mOwnerDaoClass;
    private long mOwnerId;

    protected BaseOwnedEntityCollection(final Class<OWNERDAO> ownerDaoClass,
                                        final long ownerId,
                                        final Class<OWNEDKO> ownedDomainObjectClass,
                                        final Class<OWNEDDAO> ownedDaoClass) {
        super(ownedDomainObjectClass, ownedDaoClass);
        mOwnerDaoClass = ownerDaoClass;
        mOwnerId = ownerId;
    }

    @Override
    public List<OWNEDKO> getElements() {
        List<OWNEDDAO> daos = OfyManager.getOfyService().ofy().load().type(getElementDaoClass())
                .filter("mOwnerDao", Key.create(mOwnerDaoClass, mOwnerId)).list();
        return toDomainCollection(daos);
    }
}
