package com.fizzbuzz.vroom.core.persist;

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

import com.fizzbuzz.vroom.core.domain.IdObject;

/**
 * Interface for "persist" objects that manage individual DomainObjects.
 */
public interface ObjectPersist<DO extends IdObject> {

    /**
     * Returns the PersistentObject having the provided ID
     *
     * @param id the PersistentObject's ID
     * @return the PersistentObject corresponding to the provided ID
     */
    public DO get(final long id);

    /**
     * Updates a PersistentObject's state.  Often this will require merging the state of the provided PersistentObject with the
     * existing persisted state of that object and then persisting that new, merged state.
     *
     * @param modelObject the new state for the PersistentObject
     */
    public void update(final DO modelObject);

    /**
     * Deletes the PersistentObject having the provided ID
     *
     * @param id the PersistentObject's ID
     */
    public void delete(final long id);

}