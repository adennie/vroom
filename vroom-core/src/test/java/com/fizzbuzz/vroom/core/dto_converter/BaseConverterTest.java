package com.fizzbuzz.vroom.core.dto_converter;

import org.junit.Test;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class BaseConverterTest {
    @Test(expected = IllegalStateException.class)
    public void testGetCanonicalUriPathThrowsWithTokenInUriTemplate() throws Exception {
        new BaseConverter("http://test.com", "/{token}") {}.getCanonicalUriPath();
    }
}
