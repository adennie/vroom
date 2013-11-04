package com.fizzbuzz.vroom.core.biz;

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

public interface IdObjectBiz<IO extends IdObject> {
    /**
     * Returns the IdObject having the provided ID
     *
     * @param id the IdObject's ID
     * @return the IdObject corresponding to the provided ID
     */
    public IO get(final long id);

    /**
     * Updates a IdObject's state.  Often this will require merging the state of the provided IdObject with the
     * existing state of that object.
     *
     * @param idObject the new state for the IdObject
     */
    public void update(final IO idObject);

    /**
     * Deletes the IdObject having the provided ID
     *
     * @param id the IdObject's ID
     */
    public void delete(final long id);
}
