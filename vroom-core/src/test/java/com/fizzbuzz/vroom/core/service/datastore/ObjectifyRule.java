package com.fizzbuzz.vroom.core.service.datastore;

/*
 * Copyright (c) 2013 Fizz Buzz LLC
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

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import org.junit.rules.ExternalResource;

public class ObjectifyRule extends ExternalResource {
    private OfyService mOfyService;

    @Override
    protected void before() throws Throwable {
        mOfyService = new TestOfyService();
        OfyManager.registerOfyService(mOfyService);
    }

    @Override
    protected void after() {
        ObjectifyFilter.complete();
    }

    public static class TestOfyService extends OfyService {

        @Override
        public Objectify ofy() {
            return ObjectifyService.ofy();
        }

        @Override
        public ObjectifyFactory factory() {
            return ObjectifyService.factory();
        }
    }
}
