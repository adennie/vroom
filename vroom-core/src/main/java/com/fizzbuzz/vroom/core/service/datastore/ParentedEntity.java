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

import com.fizzbuzz.vroom.core.domain.IEntityObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.domain.VroomCollection;
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService.ofy;

public abstract class ParentedEntity<
    PO extends IEntityObject,
    EO extends IEntityObject,
    DAO extends ParentedDao<EO>>
    extends VroomEntity<EO, DAO>
    implements IParentedEntity<EO> {

    private final Class<? extends VroomDao<PO>> mParentDaoClass;


    protected ParentedEntity(final Class<? extends VroomDao<PO>> parentDaoClass, final Class<EO> domainClass,
                             final Class<DAO> daoClass) {
        super(domainClass, daoClass);
        mParentDaoClass = parentDaoClass;
    }

    public LongKey allocateKey(LongKey parentKey) {
        return new LongKey(ofy().factory().allocateId(Key.create(mParentDaoClass, parentKey.get()), getDaoClass()).getId());
    }

    public List<LongKey> allocateKeys(LongKey parentKey, int num) {
        List<LongKey> result = new ArrayList<>(num);
        Iterable<Key<DAO>> keys = ofy().factory().allocateIds(Key.create(mParentDaoClass, parentKey.get()), getDaoClass(), num);
        // not very efficient, but the idea here is to avoid exposing Objectify types through this interface
        for (Key<DAO> key : keys) {
            result.add(new LongKey(key.getId()));
        }
        return result;
    }

    @Override
    public void delete(final LongKey parentKey, final LongKey childKey) {
        delete(getParentKey(parentKey), childKey.get());
    }

    @Override
    public void delete(LongKey parentKey, Collection<EO> entityObjects) {
        delete(getParentKey(parentKey), toIds(entityObjects));
    }

    @Override
    public void deleteAll(LongKey parentKey) {
        deleteAll(getParentKey(parentKey));
    }

    protected void delete(final Key<?> parentKey, final Long id) {
        ofy().delete().type(getDaoClass()).parent(parentKey).id(id).now();
    }

    protected void delete(VroomDao parentDao, Collection<Long> ids) {
        delete(parentDao.getKey(), ids);
    }

    protected void delete(Key<?> parentKey, Collection<Long> ids) {
        ofy().delete().type(getDaoClass()).parent(parentKey).ids(ids).now();
    }

    protected void deleteAll(final VroomDao parentDao) {
        deleteAll(parentDao.getKey());
    }

    protected void deleteAll(final Key<?> parentKey) {
        ofy().delete().type(getDaoClass()).parent(parentKey)
            .ids(getAllIdsWithAncestor(parentKey)).now();
    }

    protected VroomCollection<EO> getAllWithAncestor(VroomDao ancestorDao) {
        return getAllWithAncestor(ancestorDao.getKey());
    }

    protected VroomCollection<EO> getAllWithAncestor(Class<? extends VroomDao> ancestorDaoClass, LongKey ancestorKey) {
        return getAllWithAncestor(Key.create(ancestorDaoClass, ancestorKey.get()));
    }

    protected VroomCollection<EO> getAllWithAncestor(Key<?> ancestorKey) {
        List<DAO> daos = ofy().load().type(getDaoClass()).ancestor(ancestorKey).list();
        return toDomainCollection(daos);
    }

    protected List<Long> getAllIdsWithAncestor(VroomDao ancestorDao) {
        return getAllIdsWithAncestor(ancestorDao.getKey());
    }

    protected List<Long> getAllIdsWithAncestor(Class<? extends VroomDao> ancestorDaoClass, LongKey ancestorKey) {
        return getAllIdsWithAncestor(Key.create(ancestorDaoClass, ancestorKey.get()));
    }

    protected List<Long> getAllIdsWithAncestor(Key<?> ancestorKey) {
        List<Key<DAO>> keys = ofy().load().type(getDaoClass()).ancestor(ancestorKey).keys().list();
        return toIds(keys);
    }

    protected Key<?> getParentKey(final LongKey parentKey) {
        return Key.create(mParentDaoClass, parentKey.get());
    }

}
