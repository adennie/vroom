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

import com.fizzbuzz.vroom.core.biz.IdObjectBiz;
import com.fizzbuzz.vroom.core.domain.IdObject;
import com.fizzbuzz.vroom.core.dto_adapter.IdObjectAdapter;
import com.fizzbuzz.vroom.dto.Dto;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ResourceException;

/*
 * Base server resource class for objects with IDs (fetched from the URL).
 */
public abstract class IdResource<
        DTO extends Dto,
        B extends IdObjectBiz<DO>,
        DO extends IdObject>
        extends BaseResource {
    private long mId;
    private B mBiz;
    private IdObjectAdapter<DTO, DO> mDtoAdapter;

    public DTO getResource() {
        DTO result = null;
        try {
            DO domainObject = mBiz.get(mId);
            result = mDtoAdapter.toDto(domainObject);
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    public void putResource(final DTO dto) {
        try {
            // by default, return 204, since we're not returning any representation. Subclasses that override
            // putResource() can change the response status if needed.
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

            // make sure the client isn't trying to PUT a resource value with an ID that doesn't match the one
            // identified by the request URL.
            if (mDtoAdapter.getId(dto) != mId) {
                throw new IllegalArgumentException("The ID of the resource in the request body does not match the ID " +
                        "of the resource in the request URL");
            }

            mBiz.update(mDtoAdapter.toDomain(dto));
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Delete
    public void deleteResource() {
        try {
            mBiz.delete(mId);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    protected void doInit(final B biz,
                          final IdObjectAdapter<DTO, DO> dtoAdapter) throws ResourceException {

        super.doInit();

        mDtoAdapter = dtoAdapter;
        mBiz = biz;

        // get the resource ID from the URL
        mId = dtoAdapter.getId(getRequest().getOriginalRef().toString());
    }

    protected long getId() {
        return mId;
    }
}
