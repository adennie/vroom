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
import com.fizzbuzz.vroom.core.exception.NotFoundException;
import com.fizzbuzz.vroom.core.util.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fizzbuzz.vroom.core.persist.PersistManager.getOfyService;

/**
 * Abstract base class for "entity" objects that manage individual DomainObjects.
 */
public abstract class BaseEntity<DO extends IdObject, DAO extends BaseDao<DO>> implements Entity<DO> {
    private final Class<DO> mDomainClass;
    private final Class<DAO> mDaoClass;
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    protected BaseEntity(final Class<DO> domainClass, final Class<DAO> daoClass) {
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