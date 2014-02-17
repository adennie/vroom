package com.fizzbuzz.vroom.core.service.datastore;

import com.fizzbuzz.vroom.core.domain.IEntityObject;
import com.fizzbuzz.vroom.core.domain.IOwnedEntityObject;
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
 * @param <OWNERDO>  the owner domain object type
 * @param <OWNEDDO>  the owned domain object type
 */
public abstract class OwnedDao<
        OWNERDAO extends VroomDao<OWNERDO>,
        OWNERDO extends IEntityObject,
        OWNEDDO extends IOwnedEntityObject>
        extends VroomDao<OWNEDDO> {
    @Load(OfyLoadGroups.Deep.class)
    Ref<OWNERDAO> owner; // reference to the owner entity

    public void setOwnerId(final OWNEDDO owned, final Class<OWNERDAO> ownerDaoClass) {
        owner = Ref.create(Key.create(ownerDaoClass, owned.getOwnerKey().get()));
    }

    public long getOwnerId() {
        return owner.getKey().getId();
    }
}
