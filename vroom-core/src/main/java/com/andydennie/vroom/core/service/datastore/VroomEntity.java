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

import com.andydennie.vroom.core.domain.KeyedObject;
import com.andydennie.vroom.core.domain.LongKey;
import com.andydennie.vroom.core.exception.NotFoundException;
import com.andydennie.vroom.core.util.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.andydennie.vroom.core.service.datastore.OfyManager.ofy;

/**
 * Abstract base class for classes which manage datastore entities
 */
public abstract class VroomEntity<KO extends KeyedObject<LongKey>, DAO extends VroomDao<KO>> implements IEntity<KO> {
    private final Class<KO> mDomainClass;
    private final Class<DAO> mDaoClass;
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    protected VroomEntity(final Class<KO> domainClass, final Class<DAO> daoClass) {
        mDomainClass = domainClass;
        mDaoClass = daoClass;
    }

    @Override
    public void create(final KO keyedObject) {
        if (keyedObject.getKey().get() != null)
            throw new IllegalArgumentException("Cannot create an entity for a domain object with an existing ID; the " +
                    "ID will be assigned by the persistence layer on the initial save operation.");

        DAO dao = createDao(keyedObject);
        saveDao(dao);

        // saving the DAO assigned it an ID as a side-effect; copy that to the domain object
        keyedObject.setKey(new LongKey(dao.getId()));
    }

    @Override
    public KO get(final Long key) {
        return getDao(key).toDomainObject();
    }

    public boolean exists(final Long key) {
        try {
            get(key);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public void update(final KO keyedObject) {
        // this default implementation just overwrites the existing entity
        saveDao(createDao(keyedObject));
    }

    @Override
    public void delete(final Long key) {
        ofy().delete().type(mDaoClass).id(key).now();
    }

    protected DAO getDao(final Long key) {
        DAO result = ofy().load().type(mDaoClass).id(key).now();
        if (result == null) {
            throw new NotFoundException("No " + mDomainClass.getSimpleName() + " found with ID " +
                    Long.toString(key));
        }
        return result;
    }

    protected void saveDao(final DAO dao) {
        ofy().save().entity(dao).now();
    }

    /**
     * Creates a DAO that is initialized from the provided domain object
     *
     * @param keyedObject a keyed domain object
     * @return a DAO initialized from the domain object
     */
    protected DAO createDao(final KO keyedObject) {
        // instantiate the appropriate kind of DAO, using the constructor that takes a single domain object argument
        return Reflections.newInstance(mDaoClass, mDomainClass, keyedObject);
    }
}