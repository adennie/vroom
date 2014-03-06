package com.fizzbuzz.vroom.sample.webservice.service.datastore;

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

import com.fizzbuzz.vroom.core.service.datastore.OfyService;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

/**
 * This class registers all DAO classes with Objectify
 * See https://code.google.com/p/objectify-appengine/wiki/BestPractices for more info.
 */
public class SampleOfyService extends OfyService {
    static {
        ObjectifyFactory factory = ObjectifyService.factory();

        // translators must be registered *BEFORE* DAO classes
        JodaTimeTranslators.add(factory);

        factory.register(ImageDao.class);
        factory.register(PlaceDao.class);
        factory.register(UserDao.class);
        factory.register(UserLogEntryDao.class);
    }
}
