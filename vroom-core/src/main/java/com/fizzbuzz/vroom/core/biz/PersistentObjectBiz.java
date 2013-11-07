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
import com.fizzbuzz.vroom.core.persist.Entity;

public class PersistentObjectBiz<PO extends IdObject> implements IdObjectBiz<PO> {
    private Entity<PO> mPersist;

    public PersistentObjectBiz(final Entity<PO> persist) {
        mPersist = persist;
    }
    @Override
    public PO get(final long id) {
        return mPersist.get(id);
    }

    @Override
    public void update(final PO persistentObject) {
        mPersist.update(persistentObject);
    }

    @Override
    public void delete(final long id) {
        mPersist.delete(id);
    }
}
