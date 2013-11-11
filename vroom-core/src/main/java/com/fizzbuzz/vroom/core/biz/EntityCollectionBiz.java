package com.fizzbuzz.vroom.core.biz;

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

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.persist.datastore.EntityCollection;

import java.util.List;

public class EntityCollectionBiz<KO extends KeyedObject> implements CollectionBiz<KO>{
    private EntityCollection<KO> mEntityCollection;

    public EntityCollectionBiz(EntityCollection<KO> entityCollection) {
        mEntityCollection = entityCollection;
    }

    @Override
    public List<KO> getElements() {
        return mEntityCollection.getElements();
    }

    EntityCollection<KO> getEntityCollection() {
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
