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

import com.andydennie.vroom.core.domain.KeyedObject;
import com.andydennie.vroom.core.service.datastore.IEntityCollection;

import java.util.List;

/**
 * A business logic class for collections of persistent entities.  The associated persistence class is wired to an
 * object of this class via its constructor.
 * @param <KO> a KeyedObject subtype
 */
public class EntityCollectionBiz<KO extends KeyedObject> implements ICollectionBiz<KO> {
    private IEntityCollection<KO> mEntityCollection;

    public EntityCollectionBiz(IEntityCollection<KO> entityCollection) {
        mEntityCollection = entityCollection;
    }

    @Override
    public List<KO> getElements() {
        return mEntityCollection.getElements();
    }

    protected IEntityCollection<KO> getEntityCollection() {
        return mEntityCollection;
    }

    @Override
    public KO add(final KO idObject) {
        idObject.validate();
        return (KO) getEntityCollection().addElement(idObject);
    }

    @Override
    public void deleteAll() {
        getEntityCollection().deleteAll();
    }

    @Override
    public void delete(List<KO> idObjects) {
        getEntityCollection().delete(idObjects);
    }
}
