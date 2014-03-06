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

import com.googlecode.objectify.Work;

public interface IDatastoreService {

    // a paper-thin wrapper around Objectify's Work interface, for encapsulation
    public interface Transactable<T> extends Work<T> {
    }

    /**
     * Executes a Transactable task within a datastore transaction
     * @param task the task to perform
     * @param <T> the return type of the task
     * @return the return value from the task
     */
    public <T> T doInTransaction(final Transactable<T> task);
}
