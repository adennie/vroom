package com.fizzbuzz.vroom.core.api.resource;

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

import com.fizzbuzz.vroom.core.biz.ICollectionBiz;
import com.fizzbuzz.vroom.core.domain.VroomCollection;
import com.fizzbuzz.vroom.core.util.Reflections;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class VroomCollectionResource<
        DC extends VroomCollection<DO>,
        DO,
        B extends ICollectionBiz<DO>>
        extends VroomResource<DC>
        implements ICollectionResource<DC, DO> {

    private Class<DC> mCollectionClass;
    private B mBiz;
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public B getBiz() {
        return mBiz;
    }

    public DC getResource() {
        DC result = Reflections.newInstance(mCollectionClass);
        try {
            result.addAll(mBiz.getAll());
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }


    public DO postResource(final DO element) {
        try {
            mBiz.add(element);
            getResponse().setStatus(Status.SUCCESS_CREATED);
            // set the Location response header
            String uri = getElementUri(element);
            getResponse().setLocationRef(uri);
            mLogger.info("created new resource at location: {}",uri );
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return element;
    }

    public void deleteResource() {
            mBiz.deleteAll();
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

    protected void doInit(final Class<DC> collectionClass,
                          final B collectionBiz) throws ResourceException {
        mCollectionClass = collectionClass;
        mBiz = collectionBiz;
    }

    protected abstract String getElementUri(final DO element);
}
