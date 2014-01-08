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
import com.andydennie.vroom.core.service.datastore.IEntity;
import com.andydennie.vroom.core.domain.KeyedObject;
import com.andydennie.vroom.core.domain.LongKey;
import com.andydennie.vroom.core.service.datastore.IEntity;

public class EntityBiz<KO extends KeyedObject<LongKey>> implements IKeyedObjectBiz<KO, LongKey> {
    private IEntity<KO> mEntity;

    public EntityBiz(final IEntity<KO> entity) {
        mEntity = entity;
    }

    public IEntity<KO> getEntity() {
        return mEntity;
    }

    @Override
    public KO get(final LongKey key) {
        return mEntity.get(key.get());
    }

    @Override
    public KO get(final String keyString) {
        try {
            return get(new LongKey(keyString));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid key string: " + keyString + ", must be convertible to Long");
        }
    }

    @Override
    public void update(final KO idObject) {
        mEntity.update(idObject);
    }

    @Override
    public void delete(final LongKey key) {
        mEntity.delete(key.get());
    }

    @Override
    public void delete(final String keyString) {
        try {
            delete(new LongKey(keyString));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid key string: " + keyString + ", must be convertible to Long");
        }
    }
}
