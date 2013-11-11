package com.fizzbuzz.vroom.core.domain;

/**
 * Copyright (c) 2013 Fitivity Inc.
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
        return mKey.toString();
    }
}
