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
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;

import static com.andydennie.vroom.core.service.datastore.OfyManager.ofy;

/**
 * Base class for entity collections.
 */
public abstract class EntityCollection<
        EO extends IEntityObject,
        DAO extends VroomDao<EO>,
        E extends VroomEntity<EO, DAO>>
        implements IEntityCollection<EO> {

    private final E mElementEntity;

    protected EntityCollection(final E elementEntity) {
        mElementEntity = elementEntity;
    }

    @Override
    public DomainCollection<EO> getElements() {
        List<DAO> daos = ofy().load().type(mElementEntity.getDaoClass()).list();
        return toDomainCollection(daos);
    }

    @Override
    public void addElement(final EO entityObject) {
        mElementEntity.create(entityObject);
    }

    @Override
    public void deleteAll() {
        Iterable<Key<DAO>> keys = ofy().load().type(getElementDaoClass()).keys();
        deleteEntitiesByKey(keys);
    }

    @Override
    public void delete(List<EO> entityObjectCollection) {
        List<Key<DAO>> keys = new ArrayList<>();
        for (EO entityObjectObject : entityObjectCollection) {
            keys.add(Key.create(getElementDaoClass(), entityObjectObject.getKey().toString()));
        }
        deleteEntitiesByKey(keys);
    }

    protected DomainCollection<EO> toDomainCollection(List<DAO> daoCollection) {
        DomainCollection<EO> domainCollection = new DomainCollection<>();
        for (DAO dao : daoCollection) {
            domainCollection.add(dao.toDomainObject());
        }
        return domainCollection;
    }

    protected Class<DAO> getElementDaoClass() {
        return mElementEntity.getDaoClass();
    }

    private void deleteEntitiesByKey(Iterable<Key<DAO>> keys) {
        ofy().delete().keys(keys);
    }

    public E getElementEntity() {
        return mElementEntity;
    }

    public void rewriteAll() {
        List<DAO> daos = ofy().load().type(mElementEntity.getDaoClass()).list();
        ofy().save().entities(daos).now();
    }
}
