package com.fizzbuzz.vroom.core.persist;

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

import com.fizzbuzz.vroom.core.domain.IdObject;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;

import static com.fizzbuzz.vroom.core.persist.PersistManager.getOfyService;

/**
 * Base class for "persist" classes that manage entity collections.
 */
public abstract class BaseEntityCollection<
        DO extends IdObject,
        DAO extends BaseDao<DO>>
        implements EntityCollection<DO> {

    private final Class<DO> mDomainElementClass;
    private final Class<DAO> mElementDaoClass;

    protected BaseEntityCollection(final Class<DO> domainElementClass,
                                   final Class<DAO> elementDaoClass) {
        mDomainElementClass = domainElementClass;
        mElementDaoClass = elementDaoClass;
    }

    @Override
    public List<DO> getDomainElements() {
        List<DAO> daos = getOfyService().ofy().load().type(mElementDaoClass).list();
        return toDomainCollection(daos);
    }

    @Override
    public DO addElement(final DO domainObject) {

        //TODO: addElement() - seriously consider making this method return void.

        // construct an appropriate DAO from the domain object, but clear the ID since we're going to be saving a new
        // entity and we want an auto-generated ID assigned.
        DAO dao = createElementDao(domainObject);

        createElementEntity(dao);

        // saving the DAO assigned it an ID as a side-effect; copy that to the domain object
        domainObject.setId(dao.getId());

        return domainObject;
    }

    public Class<DO> getDomainElementClass() {
        return mDomainElementClass;
    }

    @Override
    public void deleteAll() {
        Iterable<Key<DAO>> keys = getOfyService().ofy().load().type(getElementDaoClass()).keys();
        deleteEntitiesByKey(keys);
    }

    @Override
    public void delete(List<DO> domainCollection) {
        List<Key<DAO>> keys = new ArrayList<Key<DAO>>();
        for (DO domainObject : domainCollection) {
            keys.add(Key.create(getElementDaoClass(), domainObject.getId()));
        }
        deleteEntitiesByKey(keys);
    }

    /**
     * Instantiates a DAO from a PersistentObject
     *
     * @param domainObject the PersistentObject
     * @return the DAO
     */
    protected DAO createElementDao(DO domainObject) {
        if (domainObject.getId() != -1)
            throw new IllegalArgumentException("Cannot create a domain object with an existing ID; the ID will be " +
                    "assigned by the persistence layer on the initial save operation.");

        // instantiate the appropriate kind of DAO, using the constructor that takes a single domain object argument
        DAO result = Reflections.newInstance(getElementDaoClass(), mDomainElementClass, domainObject);

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
        getOfyService().ofy().save().entity(dao).now();
    }

    List<DO> toDomainCollection(List<DAO> daoCollection) {
        List<DO> domainCollection = new ArrayList<DO>();
        for (DAO dao : daoCollection) {
            domainCollection.add(dao.toDomainObject());
        }
        return domainCollection;
    }

    Class<DAO> getElementDaoClass() {
        return mElementDaoClass;
    }

    private void deleteEntitiesByKey(Iterable<Key<DAO>> keys) {
        getOfyService().ofy().delete().keys(keys);
    }
}
