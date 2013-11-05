package com.fizzbuzz.vroom.core.resource;

import org.junit.Test;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class BaseResourceTest {
    @Test(expected = IllegalStateException.class)
    public void testGetCanonicalUriPathThrowsWithTokenInUriTemplate() throws Exception {
        BaseResource baseResource = new BaseResource(){};
        VroomApplication.registerRootUrl("http://test.com");
        BaseResource.registerResource(baseResource.getClass(), "/{token}");
        baseResource.getCanonicalUriPath();
    }
}
