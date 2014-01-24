package com.andydennie.vroom.core.biz;

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
import com.andydennie.vroom.core.service.datastore.IEntityCollection;

import java.util.List;

/**
 * A business logic class for collections of persistent entities.  The associated persistence class is wired to an
 * object of this class via its constructor.
 * @param <EO> a domain object type implementing the IEntityObject interface
 * @param <EC></EC> a domain collection type implementing the IEntityCollection interface
 */
public class EntityCollectionBiz<
        EO extends IEntityObject,
        EC extends IEntityCollection<EO>>
        implements ICollectionBiz<EO> {
    private EC mEntityCollection;

    public EntityCollectionBiz(EC entityCollection) {
        mEntityCollection = entityCollection;
    }

    @Override
    public List<EO> getElements() {
        return mEntityCollection.getElements();
    }

    protected EC getEntityCollection() {
        return mEntityCollection;
    }

    @Override
    public void add(final EO domainObject) {
        domainObject.validate();
        getEntityCollection().addElement(domainObject);
    }

    @Override
    public void deleteAll() {
        getEntityCollection().deleteAll();
    }

    @Override
    public void delete(List<EO> domainObjects) {
        getEntityCollection().delete(domainObjects);
    }
}
