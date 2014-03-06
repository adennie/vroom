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
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.exception.NotFoundException;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.googlecode.objectify.Key;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService.ofy;

/**
 * Abstract base class for classes which manage datastore entities
 */
public abstract class VroomEntity<EO extends IEntityObject, DAO extends VroomDao<EO>> implements IEntity<EO> {
    private final Class<EO> mDomainClass;
    private final Class<DAO> mDaoClass;



    protected VroomEntity(final Class<EO> domainClass, final Class<DAO> daoClass) {
        mDomainClass = domainClass;
        mDaoClass = daoClass;
    }

    @Override
    public void create(final EO domainObject) {

        DAO dao = createDao(domainObject);
        saveDao(dao);

        if (domainObject.getKey().get() == null) {
            // saving the DAO assigned it an ID as a side-effect; copy that to the domain object
            domainObject.setKey(new LongKey(dao.getId()));
        }
    }

    @Override
    public EO get(final Long key) {
        return loadDao(key).toDomainObject();
    }

    @Override
    public void update(final EO domainObject) {
        DAO dao = loadDao(domainObject.getKey().get());
        updateDao(dao, domainObject);
        saveDao(dao);
    }

    @Override
    public void delete(final Long key) {
        ofy().delete().type(mDaoClass).id(key).now();
    }

    @Override
    public DomainCollection<EO> getAll() {
        List<DAO> daos = ofy().load().type(getDaoClass()).list();
        return toDomainCollection(daos);
    }

    @Override
    public void deleteAll() {
        Iterable<Key<DAO>> keys = ofy().load().type(getDaoClass()).keys();
        deleteEntitiesByKey(keys);
    }

    @Override
    public void delete(List<EO> entityObjectCollection) {
        List<Key<DAO>> keys = new ArrayList<>();
        for (EO entityObjectObject : entityObjectCollection) {
            keys.add(Key.create(getDaoClass(), entityObjectObject.getKey().toString()));
        }
        deleteEntitiesByKey(keys);
    }

    public long allocateId() {
        return ofy().factory().allocateId(getDaoClass()).getId();
    }

    @Override
    public List<Long> allocateIds(int num) {
        List result = new ArrayList(num);
        Iterable<Key<DAO>> keys = ofy().factory().allocateIds(getDaoClass(), num);
        // not very efficient, but the idea here is to avoid exposing Objectify types through this interface
        for (Key<DAO> key : keys) {
            result.add(key.getId());
        }
        return result;
    }

    public boolean exists(final Long key) {
        try {
            get(key);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public void rewriteAll() {
        List<DAO> daos = ofy().load().type(getDaoClass()).list();
        ofy().save().entities(daos).now();
    }

    protected DomainCollection<EO> toDomainCollection(List<DAO> daoCollection) {
        DomainCollection<EO> domainCollection = new DomainCollection<>();
        for (DAO dao : daoCollection) {
            domainCollection.add(dao.toDomainObject());
        }
        return domainCollection;
    }

    /**
     * Updates a DAO's state (typically in preparation for saving it to the datastore).
     * Subclasses should override if they need to assign state to the DAO beyond that which the DAO can get from
     * the domain object directly via its {@link VroomDao#fromDomainObject} method. This might include calculated,
     * generated, or derived values, state pulled from other objects or services, etc.
     *
     * @param dao          a DAO which has been loaded from the datastore
     * @param domainObject a domain object containing state to be merged into the state of the DAO
     */
    protected void updateDao(final DAO dao, final EO domainObject) {
        dao.fromDomainObject(domainObject);
    }

    protected DAO loadDao(final Long key) {
        DAO result = ofy().load().type(mDaoClass).id(key).now();
        if (result == null) {
            throw new NotFoundException("No " + mDomainClass.getSimpleName() + " found with ID " +
                Long.toString(key));
        }
        return result;
    }

    protected void saveDao(final DAO dao) {
        // set the modification date, if there is a field annotated for that.
        setModDate(dao);

        ofy().save().entity(dao).now();
    }

    /**
     * Creates a DAO of the type associated with the provided domain object
     * <p/>
     * Note: if the values of any fields in the DAO need to be generated or calculated at creation time,
     * it is recommended to accomplish this be providing setters in the DAO class and calling them from an override
     * of this method.  This way, persistence-related business logic stays within the entity class (and out of the
     * DAO class).
     *
     * @param domainObject a keyed domain object
     * @return a DAO initialized from the domain object
     */
    protected DAO createDao(final EO domainObject) {
        // instantiate the appropriate kind of DAO
        DAO dao = Reflections.newInstance(mDaoClass);

        // initialize the DAO from the domain object's state
        dao.fromDomainObject(domainObject);

        // set the creation date, if there is a field annotated for that.
        setCreationDate(dao);

        return dao;
    }

    protected Class<DAO> getDaoClass() {
        return mDaoClass;
    }

    private void deleteEntitiesByKey(Iterable<Key<DAO>> keys) {
        ofy().delete().keys(keys);
    }

    private void setCreationDate(DAO dao) {
        // look for a @CreateDate annotation
        Field field = Reflections.getFirstFieldAnnotatedWith(mDaoClass, Object.class, CreateDate.class);
        if (field != null) {
            VroomDatastoreService.getDateTimeStamper(field.getType()).stampWithNow(dao, field);
        }
    }

    private void setModDate(DAO dao) {
        // look for a @ModDate annotation
        Field field = Reflections.getFirstFieldAnnotatedWith(mDaoClass, Object.class, ModDate.class);
        if (field != null) {
            VroomDatastoreService.getDateTimeStamper(field.getType()).stampWithNow(dao, field);
        }
    }
}