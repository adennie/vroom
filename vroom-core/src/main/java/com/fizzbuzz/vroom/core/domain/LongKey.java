package com.fizzbuzz.vroom.core.domain;

/**
 * Copyright (c) 2013 Fitivity Inc.
 */

public class LongKey extends KeyType<Long> {
    public LongKey(final Long key) {
        super(key);
    }

    public LongKey(final String keyAsString) {
            super(Long.parseLong(keyAsString));
    }
}
