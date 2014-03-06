package com.fizzbuzz.vroom.core.service.datastore;

/*
 * Copyright (c) 2014 Fizz Buzz LLC
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

import com.fizzbuzz.vroom.core.domain.DomainCollection;
import com.fizzbuzz.vroom.core.domain.IEntityObject;
import com.fizzbuzz.vroom.core.domain.IOwnedEntityObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.googlecode.objectify.Key;

import java.util.List;

import static com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService.ofy;

public class OwnedEntity<
        OWNERDO extends IEntityObject,
        OWNEDDO extends IOwnedEntityObject,
        OWNERDAO extends VroomDao<OWNERDO>,
        OWNEDDAO extends OwnedDao<OWNERDAO, OWNERDO, OWNEDDO>>
        extends VroomEntity<OWNEDDO, OWNEDDAO> {
    private Class<OWNERDAO> mOwnerDaoClass;
    private LongKey mOwnerKey;

    protected OwnedEntity(final Class<OWNEDDO> domainClass, final Class<OWNERDAO> ownerDaoClass,
                          final Class<OWNEDDAO> ownedDaoClass,
                          final LongKey ownerKey) {
        super(domainClass, ownedDaoClass);
        mOwnerDaoClass = ownerDaoClass;
        mOwnerKey = ownerKey;
    }

    /**
     * Updates the DAO's owner ID
     *
     * @param dao         the DAO to update
     * @param domainObject the domain object containing the new state
     */
    @Override
    protected void updateDao(final OWNEDDAO dao, final OWNEDDO domainObject) {
        super.updateDao(dao, domainObject);
        dao.setOwnerId(domainObject, mOwnerDaoClass);
    }

    @Override
    public DomainCollection<OWNEDDO> getAll() {
        List<OWNEDDAO> daos = ofy().load().type(getDaoClass())
                .filter("owner", Key.create(mOwnerDaoClass, mOwnerKey.get())).list();
        return toDomainCollection(daos);
    }
}
