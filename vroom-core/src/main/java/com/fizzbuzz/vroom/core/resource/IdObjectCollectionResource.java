package com.fizzbuzz.vroom.core.resource;

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

import com.fizzbuzz.vroom.core.biz.CollectionBiz;
import com.fizzbuzz.vroom.core.domain.DomainCollection;
import com.fizzbuzz.vroom.core.domain.IdObject;
import org.restlet.resource.ResourceException;

public abstract class IdObjectCollectionResource<
        DC extends DomainCollection<IO>,
        IO extends IdObject,
        CB extends CollectionBiz<IO>>
        extends DomainCollectionResource<DC, IO, CB> {

    private Class<? extends IdObjectResource> mElementResourceClass;

    @Override
    protected String getElementCanonicalUri(final IO element) {
        return IdObjectResource.getCanonicalUri(mElementResourceClass, element.getId());
    }

    protected void doInit(final Class<DC> domainCollectionClass, final CB collectionBiz, Class<? extends IdObjectResource> elementResourceClass)
            throws ResourceException {
        super.doInit(domainCollectionClass, collectionBiz);
        mElementResourceClass = elementResourceClass;
    }
}
