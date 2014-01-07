package com.fizzbuzz.vroom.core.api;

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

import com.fizzbuzz.vroom.core.api.application.BaseApplication;
import com.fizzbuzz.vroom.core.api.resource.BaseResource;
import org.junit.Test;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.then;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;

public class BaseResourceTest {
    @Test
    public void testGetCanonicalUriPathThrowsWithTokenInUriTemplate() throws Exception {
        // given a registered resource with a token element in its template URI
        BaseResource baseResource = new BaseResource(){};
        BaseApplication.registerRootUrl("http://test.com");
        BaseResource.registerResource(baseResource.getClass(), "/{token}");

        // when attempting to get its canonical URI path without providing a token value
        when(baseResource).getCanonicalUriPath();

        // then an IllegalStateException should be thrown
        then(caughtException())
                .isInstanceOf(IllegalStateException.class);
    }
}
