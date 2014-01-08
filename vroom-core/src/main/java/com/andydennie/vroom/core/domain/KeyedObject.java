package com.andydennie.vroom.core.domain;

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

public abstract class KeyedObject<K extends KeyType> extends DomainObject {
    private K mKey;

    public KeyedObject(final K key) {
        mKey = key;
    }

    public K getKey() {
        return mKey;
    }

    public String getKeyAsString() {
        return mKey.toString();
    }

    public void setKey(final K key) {
        mKey = key;
    }

    public void validate() {
    }
}
