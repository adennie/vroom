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
import com.andydennie.vroom.core.domain.IOwnedEntityObject;

public class OwnedEntity<
        OWNERDO extends IEntityObject,
        OWNEDDO extends IOwnedEntityObject,
        OWNERDAO extends VroomDao<OWNERDO>,
        OWNEDDAO extends OwnedDao<OWNERDAO, OWNERDO, OWNEDDO>>
        extends VroomEntity<OWNEDDO, OWNEDDAO> {
    private Class<OWNERDAO> mOwnerDaoClass;

    protected OwnedEntity(final Class<OWNEDDO> domainClass, final Class<OWNERDAO> ownerDaoClass,
                          final Class<OWNEDDAO> ownedDaoClass) {
        super(domainClass, ownedDaoClass);
        mOwnerDaoClass = ownerDaoClass;
    }

    /**
     * Updates the DAO's owner ID
     *
     * @param dao         the DAO to update
     * @param domainObject the domain object containing the new state
     */
    @Override
    protected void updateDao(final OWNEDDAO dao, final OWNEDDO domainObject) {
        super.updateDao(dao, domainObject);
        dao.setOwnerId(domainObject, mOwnerDaoClass);
    }
}
