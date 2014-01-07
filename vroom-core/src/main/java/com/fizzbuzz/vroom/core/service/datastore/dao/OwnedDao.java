package com.fizzbuzz.vroom.core.service.datastore.dao;

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.service.datastore.OfyLoadGroups;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Load;

/*
 * Copyright (c) 2014 Andy Dennie
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
 * An abstract base class for DAOs that are logically owned by another DAO. These DAOs store a reference to the
 * owner DAO.
 *
 * @param <OWNERDAO> the owner DAO type
 * @param <OWNERKO>  the owner domain object type
 * @param <OWNEDKO>  the owned domain object type
 */
public abstract class OwnedDao<OWNERDAO extends BaseDao<OWNERKO>,
        OWNERKO extends KeyedObject<LongKey>,
        OWNEDKO extends KeyedObject<LongKey>>
        extends BaseDao<OWNEDKO> {
    @Load(OfyLoadGroups.Deep.class)
    Ref<OWNERDAO> ownerRef; // reference to the owner entity

    // no-arg constructor used by Objectify
    public OwnedDao() {
    }

    public OwnedDao(final Class<OWNERDAO> ownerDaoClass, final long ownerId) {
        ownerRef = Ref.create(Key.create(ownerDaoClass, ownerId));
    }

    protected long getOwnerId() {
        return ownerRef.getKey().getId();
    }
}
