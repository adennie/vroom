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

import com.fizzbuzz.vroom.core.biz.IKeyedObjectBiz;
import com.fizzbuzz.vroom.core.domain.IKeyedObject;
import com.fizzbuzz.vroom.core.domain.KeyType;
import com.fizzbuzz.vroom.core.domain.KeyedObject;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/*
 * Base server resource class for objects with IDs (fetched from the URL).
 */
public abstract class KeyedResource<
        K extends KeyType,
        B extends IKeyedObjectBiz<KO, K>,
        KO extends IKeyedObject<K>>
        extends VroomResource<KO> {

    private String mKeyString;
    private B mBiz;

    public B getBiz() {
        return mBiz;
    }

    public KO getResource() {
        KO result = null;
        try {
            result = mBiz.get(mKeyString);
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    public void putResource(final KO keyedObject) {
        try {
            // by default, return 204, since we're not returning any representation. Subclasses that override
            // putResource() can change the response status if needed.
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

            // make sure the client isn't trying to PUT a resource value with an ID that doesn't match the one
            // identified by the request URL.
            if (!keyedObject.getKey().toString().equals(mKeyString)) {
                throw new IllegalArgumentException("The ID of the resource in the request body does not match the ID " +
                        "of the resource in the request URL");
            }

            mBiz.update(keyedObject);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    public void deleteResource() {
        try {
            mBiz.delete(mKeyString);
            // current version of Restlet returns 200 by default for successful DELETE; should be 204.
            // TODO: remove this after upgrading to Restlet 2.2 RC3, which fixes this issue.
            setStatus(Status.SUCCESS_NO_CONTENT);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    public String getCanonicalUri(final KeyedObject keyedObject) {
        return ResourceRegistry.getCanonicalUri(this.getClass(), keyedObject.getKeyAsString());
    }

    protected void doInit(final B biz) throws ResourceException {

        mBiz = biz;

        // get the resource ID from the URL
        mKeyString = getTokenValue(getIdToken());
    }

    protected String getKeyString() {
        return mKeyString;
    }

    private String getIdToken() {
        return ResourceRegistry.getIdToken(this.getClass());
    }
}
