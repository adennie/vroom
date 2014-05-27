package com.fizzbuzz.vroom.core.biz;

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
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.service.datastore.IEntity;

import java.util.Collection;

public abstract class EntityBiz<E extends IEntityObject> implements IEntityBiz<E>, ICollectionBiz<E> {
    private IEntity<E> mEntity;

    public EntityBiz(final IEntity<E> entity) {
        mEntity = entity;
    }

    public IEntity<E> getEntity() {
        return mEntity;
    }

    @Override
    public void add(final E domainObject) {
        onAdd(domainObject);
        getEntity().create(domainObject);
    }

    @Override
    public void add(Collection<E> domainObjects) {
        for (E domainObject : domainObjects) {
            onAdd(domainObject);
        }
        getEntity().create(domainObjects);
    }

    @Override
    public E get(final LongKey key) {
        return mEntity.get(key);
    }

    @Override
    public Collection<E> getAll() {
        return mEntity.getAll();
    }

    @Override
    public E get(final String keyString) {
        try {
            return get(new LongKey(keyString));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid key string: " + keyString + ", must be convertible to Long");
        }
    }

    @Override
    public void update(final E domainObject) {
        onUpdate(domainObject);
        mEntity.update(domainObject);
    }

    @Override
    public void delete(final LongKey key) {
        onDelete(mEntity.get(key));
        mEntity.delete(key);
    }

    @Override
    public void delete(final String keyString) {
        try {
            delete(new LongKey(keyString));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid key string: " + keyString + ", must be convertible to Long");
        }
    }

    @Override
    public void delete(Collection<E> domainObjects) {
        for (E domainObject : domainObjects) {
            onDelete(domainObject);
        }
        getEntity().delete(domainObjects);
    }

    @Override
    public void deleteAll() {
        Collection<E> domainObjects = getAll();
        for (E domainObject : domainObjects) {
            onDelete(domainObject);
        }
        getEntity().deleteAll();
    }

    protected void onAdd(E entityObject) {
    }
    protected void onUpdate(E entityObject) {
    }
    protected void onDelete(E entityObject) {
    }
}
