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

import com.fizzbuzz.vroom.core.domain.KeyType;
import com.fizzbuzz.vroom.core.domain.KeyedObject;

public interface IKeyedObjectBiz<KO extends KeyedObject<K>, K extends KeyType> {
    /**
     * Returns the KeyedObject having the provided ID
     *
     * @param key the KeyedObject's key
     * @return the KeyedObject corresponding to the provided key
     */
    public KO get(final K key);

    /**
     * Returns the KeyedObject having the provided key
     *
     * @param keyString the KeyedObject's key in String form
     * @return the KeyedObject corresponding to the provided key
     */
    public KO get(final String keyString);

    /**
     * Updates a KeyedObject's state.  Often this will require merging the state of the provided KeyedObject with the
     * existing state of that object.
     *
     * @param keyedObject the new state for the KeyedObject
     */
    public void update(final KO keyedObject);

    /**
     * Deletes the KeyedObject having the provided key
     *
     * @param key the KeyedObject's ID
     */
    public void delete(final K key);

    /**
     * Deletes the KeyedObject having the provided key
     *
     * @param keyString the KeyedObject's key in String form
     */
    public void delete(final String keyString);

}