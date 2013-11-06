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

import com.fizzbuzz.vroom.core.domain.DomainCollection;
import com.fizzbuzz.vroom.core.domain.IdObject;
import com.fizzbuzz.vroom.core.persist.CollectionPersist;

import java.util.List;

public class PersistentCollectionBiz<IO extends IdObject> implements CollectionBiz<IO>{
    private CollectionPersist<IO> mPersist;

    public PersistentCollectionBiz(CollectionPersist<IO> persist) {
        mPersist = persist;
    }

    @Override
    public List<IO> getElements() {
        return mPersist.getDomainElements();
    }

    CollectionPersist<IO> getPersist() {
        return mPersist;
    }

    @Override
    public IO add(final IO domainObject) {
        domainObject.validate();
        return (IO)getPersist().addElement(domainObject);
    }

    @Override
    public void deleteAll() {
        getPersist().deleteAll();
    }

    @Override
    public void delete(List<IO> domainObjects) {
        getPersist().delete(domainObjects);
    }
}
