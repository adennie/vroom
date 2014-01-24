package com.andydennie.vroom.core.service.datastore;

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

import com.andydennie.vroom.core.domain.DomainCollection;
import com.andydennie.vroom.core.domain.IEntityObject;
import com.andydennie.vroom.core.domain.IOwnedEntityObject;
import com.andydennie.vroom.core.domain.LongKey;
import com.googlecode.objectify.Key;

import java.util.List;

import static com.andydennie.vroom.core.service.datastore.OfyManager.ofy;


/**
 * A base class for managing collections of entities which are logically "owned" by another entity.
 * The owned entity stores a reference to the key of its owner, allowing the {@link #getElements()} method to
 * constrain the results to those belonging to that particular owner.
 *
 * @param <OWNEDEO>  the owned domain object type (a KeyedObject<Long>)
 * @param <OWNERDAO> the owner DAO type
 * @param <OWNEDDAO> the owned DAO type
 */
public abstract class OwnedEntityCollection<
        OWNEREO extends IEntityObject,
        OWNEDEO extends IOwnedEntityObject,
        OWNERDAO extends VroomDao<OWNEREO>,
        OWNEDDAO extends OwnedDao<OWNERDAO, OWNEREO, OWNEDEO>,
        E extends VroomEntity<OWNEDEO, OWNEDDAO>>
        extends EntityCollection<OWNEDEO, OWNEDDAO, E> {

    private Class<OWNERDAO> mOwnerDaoClass;
    private LongKey mOwnerKey;

    protected OwnedEntityCollection(final Class<OWNERDAO> ownerDaoClass,
                                    final LongKey ownerKey,
                                    final E elementEntity) {
        super(elementEntity);
        mOwnerDaoClass = ownerDaoClass;
        mOwnerKey = ownerKey;
    }

    @Override
    public DomainCollection<OWNEDEO> getElements() {
        List<OWNEDDAO> daos = ofy().load().type(getElementDaoClass())
                .filter("owner", Key.create(mOwnerDaoClass, mOwnerKey.get())).list();
        return toDomainCollection(daos);
    }
}
