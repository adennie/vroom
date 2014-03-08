package com.fizzbuzz.vroom.sample.webservice.api.resource;

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
import com.fizzbuzz.vroom.sample.webservice.api.application.MediaTypes;
import org.restlet.resource.Get;

public class NonDomainResource extends VroomResource<NonDomainResource.NonDomainDto> {

    @Override
    @Get(MediaTypes.NonDomainResultMediaTypes.JSON_V1_0 + "|json")
    public NonDomainDto getResource() {
        return new NonDomainDto(123);
    }

    public static class NonDomainDto {
        public NonDomainDto(final int foo) {
            this.foo = foo;
        }

        public int getFoo() {
            return foo;
        }

        public void setFoo(int foo) {
            this.foo = foo;
        }

        private int foo;
    }
}
