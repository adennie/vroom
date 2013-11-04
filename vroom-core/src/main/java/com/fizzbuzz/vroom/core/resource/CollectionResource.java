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
import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.dto_adapter.CollectionAdapter;
import com.fizzbuzz.vroom.core.dto_adapter.ObjectAdapter;
import com.fizzbuzz.vroom.dto.CollectionDto;
import com.fizzbuzz.vroom.dto.Dto;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.ResourceException;

import java.util.List;

public abstract class CollectionResource<
        DTC extends CollectionDto<DTO>,
        DTO extends Dto,
        CB extends CollectionBiz<DO>,
        DO extends DomainObject>
        extends BaseResource {
    private CollectionBiz<DO> mCollectionBiz;
    private CollectionAdapter<DTC, DTO, DO> mCollectionAdapter;
    private ObjectAdapter<DTO, DO> mElementAdapter;

    public DTC getResource() {
        DTC result = null;
        try {
            result = mCollectionAdapter.toDto(mCollectionBiz.getElements());
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    public DTO postResource(final DTO elementDto) {
        DTO result = null;
        try {
            // when creating a new resource, the DTO's selfRef field must be null
            if (!(elementDto.getSelfRef() == null)) {
                throw new IllegalArgumentException("when creating a new resource, the value of the uri field must be " +
                        "null");
            }
            DO domainObject =
                    mCollectionBiz.add(mElementAdapter.toDomain(elementDto));
            result = mElementAdapter.toDto(domainObject);
            getResponse().setStatus(Status.SUCCESS_CREATED);
            getResponse().setLocationRef(mElementAdapter.getCanonicalUriPath(domainObject));
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
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
        // specify the
        // Content-Location header to indicate the URI of that resource. The value of the URI was already stored into
        // the LocationRef of the response, so just grab that and reuse it.
        if (getMethod().equals(Method.POST))
            result.setLocationRef(getResponse().getLocationRef());
        return result;
    }

    protected void doInit(final CB collectionBiz,
                          final CollectionAdapter<DTC, DTO, DO> collectionAdapter,
                          final ObjectAdapter<DTO, DO> elementAdapter) throws ResourceException {
        mCollectionBiz = collectionBiz;
        mCollectionAdapter = collectionAdapter;
        mElementAdapter = elementAdapter;
    }

    protected CollectionAdapter<DTC, DTO, DO> getCollectionAdapter() {
        return mCollectionAdapter;
    }

    protected List<DO> getDomainCollection() {
        return mCollectionBiz.getElements();
    }
}
