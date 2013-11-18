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
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.persist.datastore.entity.Entity;

public class EntityBiz<KO extends KeyedObject<LongKey>> implements KeyedObjectBiz<KO, LongKey> {
    private Entity<KO> mEntity;

    public EntityBiz(final Entity<KO> entity) {
        mEntity = entity;
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
