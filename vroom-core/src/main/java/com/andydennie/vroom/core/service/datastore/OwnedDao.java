package com.andydennie.vroom.core.service.datastore;

import com.andydennie.vroom.core.domain.KeyedObject;
import com.andydennie.vroom.core.domain.LongKey;
import com.andydennie.vroom.core.domain.OwnedObject;
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
public abstract class OwnedDao<OWNERDAO extends Dao<OWNERKO>,
        OWNERKO extends KeyedObject<LongKey>,
        OWNEDKO extends OwnedObject<LongKey>>
        extends Dao<OWNEDKO> {
    @Load(OfyLoadGroups.Deep.class)
    Ref<OWNERDAO> owner; // reference to the owner entity

    // no-arg constructor used by Objectify
    protected OwnedDao() {
    }

    protected OwnedDao(final OWNEDKO owned, final Class<OWNERDAO> ownerDaoClass) {
        super(owned);
        owner = Ref.create(Key.create(ownerDaoClass, owned.getParentKey().get()));
    }

    protected long getOwnerId() {
        return owner.getKey().getId();
    }
}
