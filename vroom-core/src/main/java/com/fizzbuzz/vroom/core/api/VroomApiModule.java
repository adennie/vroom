package com.fizzbuzz.vroom.core.api;

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

import com.fizzbuzz.vroom.core.api.resource.VroomResource;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Module(injects = VroomResource.class)
public class VroomApiModule {
    @Provides  @Strict
    MemcacheService provideMemcacheService() {
        MemcacheService result = MemcacheServiceFactory.getMemcacheService();
        result.setErrorHandler(ErrorHandlers.getStrict());
        return result;
    }

    // a qualifier used with the provideMemcacheService provider to indicate that it is providing a MemcacheService
    // configured with a "strict" error handler
    @Qualifier
    @Target({FIELD, METHOD, PARAMETER})
    @Documented
    @Retention(RUNTIME)
    public @interface Strict {
    }
}
