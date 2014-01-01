package com.fizzbuzz.vroom.core.api;

import com.fizzbuzz.vroom.core.api.application.VroomApplication;
import com.fizzbuzz.vroom.core.api.resource.VroomResource;
import org.junit.Test;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.then;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class VroomResourceTest {
    @Test
    public void testGetCanonicalUriPathThrowsWithTokenInUriTemplate() throws Exception {
        // given a registered resource with a token element in its template URI
        VroomResource vroomResource = new VroomResource(){};
        VroomApplication.registerRootUrl("http://test.com");
        VroomResource.registerResource(vroomResource.getClass(), "/{token}");

        // when attempting to get its canonical URI path without providing a token value
        when(vroomResource).getCanonicalUriPath();

        // then an IllegalStateException should be thrown
        then(caughtException())
                .isInstanceOf(IllegalStateException.class);
    }
}
