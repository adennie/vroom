package com.andydennie.vroom.core.api.resource;

/*
 * Copyright (c) 2014 Andy Dennie
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

import com.andydennie.vroom.core.biz.ICollectionBiz;
import com.andydennie.vroom.core.domain.DomainCollection;
import com.andydennie.vroom.core.domain.KeyedObject;
import com.andydennie.vroom.core.biz.ICollectionBiz;
import com.andydennie.vroom.core.domain.DomainCollection;
import com.andydennie.vroom.core.domain.KeyedObject;
import org.restlet.resource.ResourceException;

public abstract class KeyedObjectCollectionResource<DC extends DomainCollection<KO>, KO extends KeyedObject>
        extends DomainCollectionResource<DC, KO> {

    private Class<? extends KeyedObjectResource> mElementResourceClass;

    @Override
    protected String getElementCanonicalUri(final KO element) {
        return KeyedObjectResource.getCanonicalUri(mElementResourceClass, element.getKeyAsString());
    }

    protected void doInit(final Class<DC> domainCollectionClass,
                          final ICollectionBiz<KO> collectionBiz,
                          final Class<? extends KeyedObjectResource> elementResourceClass)
            throws ResourceException {
        super.doInit(domainCollectionClass, collectionBiz);
        mElementResourceClass = elementResourceClass;
    }
}
