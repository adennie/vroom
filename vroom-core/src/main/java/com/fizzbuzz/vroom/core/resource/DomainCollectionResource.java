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
import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.util.Reflections;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.ResourceException;

import java.util.List;

public abstract class DomainCollectionResource<
        DC extends DomainCollection<DO>,
        DO extends DomainObject,
        CB extends CollectionBiz<DO>>
        extends BaseResource {
    private Class<DC> mDomainCollectionClass;
    private CB mCollectionBiz;

    public List<DO> getResource() {
        DC result = Reflections.newInstance(mDomainCollectionClass);
        try {
            result.addAll(mCollectionBiz.getElements());
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    public DO postResource(final DO element) {
        DO idObject = null;
        try {
            idObject = mCollectionBiz.add(element);
            getResponse().setStatus(Status.SUCCESS_CREATED);
            // set the Location response header
            getResponse().setLocationRef(getElementCanonicalUri(element));
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return idObject;
    }

    @Delete
    public void deleteResource() {
        mCollectionBiz.deleteAll();
    }

    @Override
    public Representation toRepresentation(final Object source,
                                           final Variant target) {
        Representation result = super.toRepresentation(source, target);
        // the POST method creates a new collection element, which is returned as the response body. We should
        // specify the Content-Location header to indicate the URI of that resource. The value of the URI was already
        // stored into
        // the LocationRef of the response, so just grab that and reuse it.
        if (getMethod().equals(Method.POST))
            result.setLocationRef(getResponse().getLocationRef());
        return result;
    }

    protected void doInit(final Class<DC> domainCollectionClass, final CB collectionBiz) throws ResourceException {
        mDomainCollectionClass = domainCollectionClass;
        mCollectionBiz = collectionBiz;
    }

    protected abstract String getElementCanonicalUri(final DO element);
}
