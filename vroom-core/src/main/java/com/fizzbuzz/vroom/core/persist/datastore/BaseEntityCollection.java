package com.fizzbuzz.vroom.core.persist.datastore;

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

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for entity collections.
 */
public abstract class BaseEntityCollection<
        KO extends KeyedObject<LongKey>,
        DAO extends BaseDao<KO>>
        implements EntityCollection<KO> {

    private final Class<KO> mDomainElementClass;
    private final Class<DAO> mElementDaoClass;

    protected BaseEntityCollection(final Class<KO> domainElementClass,
                                   final Class<DAO> elementDaoClass) {
        mDomainElementClass = domainElementClass;
        mElementDaoClass = elementDaoClass;
    }

    @Override
    public List<KO> getElements() {
        List<DAO> daos = OfyManager.getOfyService().ofy().load().type(mElementDaoClass).list();
        return toDomainCollection(daos);
    }

    @Override
    public KO addElement(final KO keyedObject) {

        //TODO: addElement() - seriously consider making this method return void.

        // construct an appropriate DAO from the domain object, but clear the ID since we're going to be saving a new
        // entity and we want an auto-generated ID assigned.
        DAO dao = createElementDao(keyedObject);

        createElementEntity(dao);

        // saving the DAO assigned it an ID as a side-effect; copy that to the domain object
        keyedObject.setKey(new LongKey(dao.getId()));

        return keyedObject;
    }

    public Class<KO> getDomainElementClass() {
        return mDomainElementClass;
    }

    @Override
    public void deleteAll() {
        Iterable<Key<DAO>> keys = OfyManager.getOfyService().ofy().load().type(getElementDaoClass()).keys();
        deleteEntitiesByKey(keys);
    }

    @Override
    public void delete(List<KO> keyedObjectCollection) {
        List<Key<DAO>> keys = new ArrayList<Key<DAO>>();
        for (KO keyedObject : keyedObjectCollection) {
            keys.add(Key.create(getElementDaoClass(), keyedObject.getKey().toString()));
        }
        deleteEntitiesByKey(keys);
    }

    /**
     * Instantiates a DAO from a PersistentObject
     *
     * @param keyedObject the PersistentObject
     * @return the DAO
     */
    protected DAO createElementDao(KO keyedObject) {
        if (keyedObject.getKey().get() != -1)
            throw new IllegalArgumentException("Cannot create a domain object with an existing ID; the ID will be " +
                    "assigned by the persistence layer on the initial save operation.");

        // instantiate the appropriate kind of DAO, using the constructor that takes a single domain object argument
        DAO result = Reflections.newInstance(getElementDaoClass(), mDomainElementClass, keyedObject);

        // the DAO should have a null ID prior to saving it, so that the datastore will auto-assign one
        result.clearId();

        return result;
    }

    /**
     * Creates a new Entity in the datastore
     *
     * @param dao
     */
    protected void createElementEntity(DAO dao) {
        OfyManager.getOfyService().ofy().save().entity(dao).now();
    }

    List<KO> toDomainCollection(List<DAO> daoCollection) {
        List<KO> domainCollection = new ArrayList<KO>();
        for (DAO dao : daoCollection) {
            domainCollection.add(dao.toDomainObject());
        }
        return domainCollection;
    }

    Class<DAO> getElementDaoClass() {
        return mElementDaoClass;
    }

    private void deleteEntitiesByKey(Iterable<Key<DAO>> keys) {
        OfyManager.getOfyService().ofy().delete().keys(keys);
    }
}
