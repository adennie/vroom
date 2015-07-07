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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;

public abstract class VroomDao<EO extends IEntityObject> {
    @Id private Long id;

    /**
     * Creates a domain object of the type associated with this DAO class and initializes it from the state of the DAO.
     *
     * @return the domain object
     */
    public abstract EO toDomainObject();

    /**
     * Updates the state of the DAO using information taken from a domain object.  This method is used both
     * when creating new datastore entities and when updating existing ones.  Subclasses should override as needed
     * to assign values to fields based on the domain object's state.
     * <p/>
     * Note: if the values of any fields in the DAO need to be generated or calculated, it is recommended to
     * accomplish this by providing setters in the DAO class and calling them from the Entity class' {@link
     * VroomEntity#createDao} and/or {@link VroomEntity#updateDao} overrides.  This way,
     * persistence-related business logic is kept in one class.
     *
     * @param domainObject the domain object
     */
    public void fromDomainObject(final EO domainObject) {
        id = domainObject.getKey().get(); // might be null
    }

    /**
     * Returns this entity's ID.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns this entity's key.
     *
     * @return the key
     */
    public Key<?> getKey() {
        return Key.create(getClass(), id);
    }
}
