package com.fizzbuzz.vroom.core.domain;

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

public abstract class KeyType<T> {
    private final T mKey;

    public KeyType(final T key) {
        mKey = key;
    }

    public T get() {
        return mKey;
    }

    public String toString() {
        return (mKey ==null)?null:mKey.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyType keyType = (KeyType) o;

        if (mKey != null ? !mKey.equals(keyType.mKey) : keyType.mKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mKey != null ? mKey.hashCode() : 0;
    }
}
