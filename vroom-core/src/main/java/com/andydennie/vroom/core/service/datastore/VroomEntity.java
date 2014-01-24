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

import com.andydennie.vroom.core.domain.IEntityObject;
import com.andydennie.vroom.core.domain.LongKey;
import com.andydennie.vroom.core.exception.NotFoundException;
import com.andydennie.vroom.core.util.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.andydennie.vroom.core.service.datastore.OfyManager.ofy;

/**
 * Abstract base class for classes which manage datastore entities
 */
public abstract class VroomEntity<EO extends IEntityObject, DAO extends VroomDao<EO>> implements IEntity<EO> {
    private final Class<EO> mDomainClass;
    private final Class<DAO> mDaoClass;
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    protected VroomEntity(final Class<EO> domainClass, final Class<DAO> daoClass) {
        mDomainClass = domainClass;
        mDaoClass = daoClass;
    }

    @Override
    public void create(final EO domainObject) {

        DAO dao = createDao(domainObject);
        saveDao(dao);

        // saving the DAO assigned it an ID as a side-effect; copy that to the domain object
        domainObject.setKey(new LongKey(dao.getId()));
    }

    @Override
    public EO get(final Long key) {
        return loadDao(key).toDomainObject();
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
    public void update(final EO domainObject) {
        DAO dao = loadDao(domainObject.getKey().get());
        updateDao(dao, domainObject);
        saveDao(dao);
    }

    @Override
    public void delete(final Long key) {
        ofy().delete().type(mDaoClass).id(key).now();
    }

    /**
     * Updates a DAO's state (typically in preparation for saving it to the datastore).
     * Subclasses should override if they need to assign state to the DAO beyond that which the DAO can get from
     * the domain object directly via its {@link VroomDao#fromDomainObject} method. This might include calculated,
     * generated, or derived values, state pulled from other objects or services, etc.
     *
     * @param dao         a DAO which has been loaded from the datastore
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
        if (domainObject.getKey().get() != null)
            throw new IllegalArgumentException("Cannot create an entity for a domain object with an existing ID; the " +
                    "ID will be assigned by the persistence layer on the initial save operation.");

        // instantiate the appropriate kind of DAO
        DAO dao = Reflections.newInstance(mDaoClass);

        // initialize the DAO from the domain object's state
        dao.fromDomainObject(domainObject);
        return dao;
    }

    Class<DAO> getDaoClass() {
        return mDaoClass;
    }
}