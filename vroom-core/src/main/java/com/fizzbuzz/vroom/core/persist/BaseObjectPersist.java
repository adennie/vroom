package com.fizzbuzz.vroom.core.persist;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.exception.NotFoundException;
import com.fizzbuzz.vroom.core.util.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fizzbuzz.vroom.core.persist.PersistManager.getOfyService;

/**
 * Interface for "persist" objects that manage individual DomainObjects.
 */
public abstract class BaseObjectPersist<DO extends DomainObject, DAO extends BaseDao<DO>> implements ObjectPersist<DO> {
    private final Class<DO> mDomainClass;
    private final Class<DAO> mDaoClass;
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    protected BaseObjectPersist(final Class<DO> domainClass, final Class<DAO> daoClass) {
        mDomainClass = domainClass;
        mDaoClass = daoClass;
    }

    @Override
    public DO get(final long id) {
        DAO dao = getOfyService().ofy().load().type(mDaoClass).id(id).now();
        if (dao == null) {
            throw new NotFoundException("No "  + mDomainClass.getSimpleName() + " found with ID " +
                    Long.toString(id));
        }
        return dao.toDomainObject();
    }

    public boolean exists(final long id) {
        try {
            get(id);
            return true;
        }
        catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public void update(final DO domainObject) {
        // instantiate the appropriate kind of DAO, using the constructor that takes a single domain object argument
        DAO dao = Reflections.newInstance(mDaoClass, mDomainClass, domainObject);
        getOfyService().ofy().save().entity(dao).now();
   }

    @Override
    public void delete(final long id) {
        getOfyService().ofy().delete().type(mDaoClass).id(id).now();
    }

}