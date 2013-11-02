package com.fizzbuzz.vroom.core.persist;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;

import static com.fizzbuzz.vroom.core.persist.PersistManager.getOfyService;

/**
 * Base class for "persist" classes that manage entity collections.
 */
public abstract class BaseCollectionPersist<
        DO extends DomainObject,
        DAO extends BaseDao<DO>>
        implements CollectionPersist<DO> {

    private final Class<DO> mDomainClass;
    private final Class<DAO> mDaoClass;

    protected BaseCollectionPersist(final Class<DO> domainObjectClass,
                                    final Class<DAO> daoClass) {
        mDomainClass = domainObjectClass;
        mDaoClass = daoClass;
    }

    @Override
    public List<DO> getDomainElements() {
        List<DAO> daos = getOfyService().ofy().load().type(mDaoClass).list();
        return toDomainCollection(daos);
    }

    @Override
    public DO add(final DO domainObject) {

        //TODO: add() - seriously consider making this method return void.

        // construct an appropriate DAO from the domain object, but clear the ID since we're going to be saving a new
        // entity and we want an auto-generated ID assigned.
        DAO dao = createDao(domainObject);

        addDao(dao);

        // saving the DAO assigned it an ID as a side-effect; copy that to the domain object
        domainObject.setId(dao.getId());

        return domainObject;
    }

    public Class<DO> getDomainClass() {
        return mDomainClass;
    }

    @Override
    public void deleteAll() {
        Iterable<Key<DAO>> keys = getOfyService().ofy().load().type(getDaoClass()).keys();
        deleteKeys(keys);
    }

    @Override
    public void delete(List<DO> domainCollection) {
        List<Key<DAO>> keys = new ArrayList<Key<DAO>>();
        for (DO domainObject : domainCollection) {
            keys.add(Key.create(getDaoClass(), domainObject.getId()));
        }
        deleteKeys(keys);
    }

    /**
     * Instantiates a DAO from a DomainObject
     *
     * @param domainObject the DomainObject
     * @return the DAO
     */
    protected DAO createDao(DO domainObject) {
        if (domainObject.getId() != -1)
            throw new IllegalArgumentException("Cannot create a domain object with an existing ID; the ID will be " +
                    "assigned by the persistence layer on the initial save operation.");

        // instantiate the appropriate kind of DAO, using the constructor that takes a single domain object argument
        DAO result = Reflections.newInstance(getDaoClass(), mDomainClass, domainObject);

        // the DAO should have a null ID prior to saving it, so that the datastore will auto-assign one
        result.clearId();

        return result;
    }

    /**
     * Creates a new Entity in the datastore
     *
     * @param dao
     */
    protected void addDao(DAO dao) {
        getOfyService().ofy().save().entity(dao).now();
    }

    List<DO> toDomainCollection(List<DAO> daoCollection) {
        List<DO> domainCollection = new ArrayList<DO>();
        for (DAO dao : daoCollection) {
            domainCollection.add(dao.toDomainObject());
        }
        return domainCollection;
    }

    Class<DAO> getDaoClass() {
        return mDaoClass;
    }

    private void deleteKeys(Iterable<Key<DAO>> keys) {
        getOfyService().ofy().delete().keys(keys);
    }
}
