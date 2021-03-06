package com.fizzbuzz.vroom.core.service.datastore;

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

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import org.junit.rules.ExternalResource;

import java.io.Closeable;
import java.io.IOException;

/**
 * Implementation of a JUnit rule which does Objectify setup and teardown logic.
 */
public class ObjectifyRule extends ExternalResource {
    private OfyService mOfyService;
    private Closeable mOfySession;

    public ObjectifyRule(final OfyService ofyService) {
        mOfyService = ofyService;
    }

    @Override
    protected void before() throws Throwable {
        VroomDatastoreService.registerOfyService(mOfyService);
        mOfySession = mOfyService.begin();
    }

    @Override
    protected void after() {
        try {
            mOfySession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
