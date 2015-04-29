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

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import java.io.Closeable;

/**
 * This is a base class used for accessing Objectify.  Subclasses should register all @Entity-annotated classes
 * in a static initialization block, for example:
 * <pre>
 * public class MyOfyService extends OfyService {
 *   static {
 *     factory().register(Thing.class);
 *     factory().register(OtherThing.class);
 *   }
 * }
 * </pre>
 *
 * The combination of the static initialization block and consistent use of this class' ofy() method throughout the
 * rest of the code ensures that no interactions with Objectify happen before the DAO classes have been registered.
 *
 * Subclasses of VroomApplication must override getOfyService() and return an instance of that subclass of OfyService.
 * VroomApplication.start() registers that instance with OfyManager, providing access to it from other Vroom
 * code.
 */
public abstract class OfyService {
    public Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public Closeable begin() {return ObjectifyService.begin();}

    public ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
