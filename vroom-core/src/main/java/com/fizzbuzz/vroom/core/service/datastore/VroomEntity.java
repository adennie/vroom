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
import com.fizzbuzz.vroom.core.exception.NotFoundException;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        onCreate(domainObject);

        DAO dao = createDao(domainObject);
        saveDao(dao);

        if (domainObject.getKey().get() == null) {
            // saving the DAO assigned it an ID as a side-effect; copy that to the domain object
            domainObject.setKey(new LongKey(dao.getId()));
        }
    }

    @Override
    public void create(Collection<EO> domainObjects) {
        List<DAO> daos = new ArrayList<>();
        for (EO domainObject : domainObjects) {
            onCreate(domainObject);
            daos.add(createDao(domainObject));
        }
        saveDaos(daos);
    }

    @Override
    public EO get(final Long id) {
        return loadDao(id).toDomainObject();
    }

    @Override
    public VroomCollection<EO> getAll() {
        return toDomainCollection(loadAllDaos());
    }

    @Override
    public List<Long> getAllIds() {
        List<Key<DAO>> keys = ofy().load().type(getDaoClass()).keys().list();
        return toIds(keys);
    }

    @Override
    public void update(final EO domainObject) {
        onUpdate(domainObject);
        DAO dao = loadDao(domainObject.getKey().get());
        updateDao(dao, domainObject);
        saveDao(dao);
    }

    @Override
    public void update(Collection<EO> domainObjects) {

        List<DAO> daos = new ArrayList<>();
        Map<Long, DAO> map = loadDaos(domainObjects);
        for (EO eo : domainObjects) {
            onUpdate(eo);
            DAO dao = map.get(eo.getKey().get());
            updateDao(dao, eo);
            daos.add(dao);
        }

        saveDaos(daos);
    }

    @Override
    public void delete(final Long key) {
        EO domainObject = get(key);
        delete(domainObject);
    }

    @Override
    public void delete(EO domainObject) {
        onDelete(domainObject);
        DAO dao = loadDao(domainObject);
        onDeleteDao(dao);
        ofy().delete().entity(dao).now();
    }

    @Override
    public void deleteAll() {
        delete(getAll());
    }

    @Override
    public void delete(Collection<EO> entityObjectCollection) {
        List<Key<DAO>> keys = new ArrayList<>();
        for (EO domainObject : entityObjectCollection) {
            onDelete(domainObject);
            DAO dao = loadDao(domainObject);
            onDeleteDao(dao);
            keys.add(Key.create(getDaoClass(), domainObject.getKey().toString()));
        }
        delete(keys);
    }

    @Override
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

    /**
     * Checks for existence of an un-parented entity.
     *
     * @param id the ID of the entity
     * @return true if the entity exists, else false
     */
    public boolean exists(final Long id, final boolean transactionless) {
        try {
            loadDao(Key.create(mDaoClass, id), transactionless);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public boolean exists(final Long id) {
        return exists(id, false);
    }

    public boolean exists(final EO domainObject) {
        try {
            loadDao(createDao(domainObject).getKey());
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public void rewriteAll() {
        List<DAO> daos = ofy().load().type(getDaoClass()).list();
        ofy().save().entities(daos).now();
    }

    protected List<DAO> loadAllDaos() {
        return ofy().load().type(getDaoClass()).list();
    }

    protected VroomCollection<EO> toDomainCollection(List<DAO> daoCollection) {
        VroomCollection<EO> domainCollection = new VroomCollection<>();
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

    protected DAO loadDao(final Long id) {
        DAO result = ofy().load().type(mDaoClass).id(id).now();
        if (result == null) {
            throw new NotFoundException("No " + mDomainClass.getSimpleName() + " found with ID " +
                    Long.toString(id));
        }
        return result;
    }

    protected DAO loadDao(final Key key, final boolean transactionless) {
        DAO result = transactionless
                ? (DAO) ofy().transactionless().load().key(key).now()
                : (DAO) ofy().load().key(key).now();
        if (result == null) {
            throw new NotFoundException("No " + mDomainClass.getSimpleName() + " found with key " +
                    key.toString());
        }
        return result;
    }

    protected DAO loadDao(final Key key) {
        return loadDao(key, false);
    }

    protected Map<Long, DAO> loadDaosWithIds(final Collection<Long> ids) {
        return ofy().load().type(mDaoClass).ids(ids);
    }

    protected Map<Long, DAO> loadDaos(final Collection<EO> domainObjects) {
        return loadDaosWithIds(toIds(domainObjects));
    }

    /**
     * Loads an (unparented) entity corresponding to a domain object.  Note: subclasses which manage parented entities
     * should override and invoke loadParentedDao()
     *
     * @param domainObject
     * @return
     */
    protected DAO loadDao(final EO domainObject) {
        return loadDao(domainObject.getKey().get());
    }

    protected DAO loadParentedDao(final Class<? extends VroomDao> parentDaoClass, final Long parentKey,
                                  EO domainObject) {
        DAO result = ofy().load().type(mDaoClass).parent(Key.create(parentDaoClass,
                parentKey)).id(domainObject.getKey().get()).now();
        return result;
    }

    protected void saveDao(final DAO dao) {
        // set the modification date, if there is a field annotated for that.
        setModDate(dao);
        onSaveDao(dao); // just about to save; make sure everything is good
        ofy().save().entity(dao).now();
    }

    protected void saveDaos(final Collection<DAO> daos) {
        for (DAO dao : daos) {
            setModDate(dao);
            onSaveDao(dao); // just about to save; make sure everything is good
        }
        ofy().save().entities(daos).now();
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

    protected List<Long> toIds(final Iterable<Key<DAO>> keys) {
        List<Long> result = new ArrayList<>();
        for (Key<DAO> key : keys) {
            result.add(key.getId());
        }
        return result;
    }

    protected void onCreate(EO domainObject) {
        domainObject.validate();
    }

    protected void onUpdate(EO domainObject) {
        domainObject.validate();
    }

    protected void onDelete(EO domainObject) {
    }

    protected void onSaveDao(DAO dao) {
    }

    protected void onDeleteDao(DAO dao) {
        checkForInboundRefs(dao);
    }

    protected List<Long> toIds(final Collection<EO> entityObjects) {
        List<Long> result = new ArrayList<>();
        for (EO eo : entityObjects) {
            result.add(eo.getKey().get());
        }
        return result;
    }

    protected void checkForInboundRefs(DAO dao) {
        //  inspect the class hierarchy looking for @InboundRef and @InboundRefs annotations, collecting them up.
        Collection<InboundRef> inboundRefList = Reflections.getClassAnnotations(mDaoClass, Object.class,
                InboundRef.class);
        Collection<InboundRefs> inboundRefsList = Reflections.getClassAnnotations(mDaoClass, Object.class,
                InboundRefs.class);
        for (InboundRefs inboundRefs : inboundRefsList) {
            for (InboundRef ref :inboundRefs.value()) {
                inboundRefList.add(ref);
            }
        }

        boolean foundRefs = false;
        StringBuilder refsBuilder = new StringBuilder();
        for (InboundRef ref : inboundRefList) {
            // look for entities of the referring DAO class with the referring field's value pointing to "dao"
            List<VroomDao> referringDaos = (List<VroomDao>)ofy().load().type(ref.daoClass())
                    .filter(ref.fieldName(), Ref.create(dao.getKey())).limit(10).list();
            if (!referringDaos.isEmpty()) {
                foundRefs = true;
                for (VroomDao referringDao : referringDaos) {
                    refsBuilder.append(referringDao.getClass().getSimpleName()
                            + " (ID= " + referringDao.getId() + ")\n");
                }
            }
        }
        if (foundRefs) {
            String reason = "The " + mDaoClass.getSimpleName() + " with ID " + dao.getId()
                    + " is referenced by another entity or entities, and therefore cannot be deleted.\n"
                    + "The following entities refer to this one (first 10 of each referring entity kind shown)\n"
                    + refsBuilder.toString();
            throw new IllegalStateException(reason);
        }
    }

    private void delete(Iterable<Key<DAO>> keys) {
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