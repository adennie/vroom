package com.fizzbuzz.vroom.core.dto_adapter;

import org.junit.Test;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class BaseAdapterTest {
    @Test(expected = IllegalStateException.class)
    public void testGetCanonicalUriThrowsWithTokenInUriTemplate() throws Exception {
        new BaseAdapter("http://test.com/{token}") {
        }.getCanonicalUri();
    }
}
