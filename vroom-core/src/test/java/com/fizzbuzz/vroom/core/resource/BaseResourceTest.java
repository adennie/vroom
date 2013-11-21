package com.fizzbuzz.vroom.core.resource;

import org.junit.Test;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.then;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class BaseResourceTest {
    @Test
    public void testGetCanonicalUriPathThrowsWithTokenInUriTemplate() throws Exception {
        // given a registered resource with a token element in its template URI
        BaseResource baseResource = new BaseResource(){};
        VroomApplication.registerRootUrl("http://test.com");
        BaseResource.registerResource(baseResource.getClass(), "/{token}");

        // when attempting to get its canonical URI path without providing a token value
        when(baseResource).getCanonicalUriPath();

        // then an IllegalStateException should be thrown
        then(caughtException())
                .isInstanceOf(IllegalStateException.class);
    }
}
