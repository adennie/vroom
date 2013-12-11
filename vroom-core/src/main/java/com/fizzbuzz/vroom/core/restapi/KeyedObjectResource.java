package com.fizzbuzz.vroom.core.restapi;

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

import com.fizzbuzz.vroom.core.biz.KeyedObjectBiz;
import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.restapi.application.BaseApplication;
import com.google.common.collect.ImmutableMap;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ResourceException;

import java.util.HashMap;
import java.util.Map;

/*
 * Base server resource class for objects with IDs (fetched from the URL).
 */
public abstract class KeyedObjectResource<
        B extends KeyedObjectBiz<KO, ?>,
        KO extends KeyedObject<?>>
        extends DomainResource<KO> {

    private static Map<Class<? extends KeyedObjectResource>, String> mResourceClassToIdTokenMap = new HashMap<>();
    private String mKeyString;
    private B mBiz;

    public static <IR extends KeyedObjectResource> void registerIdToken(
            final Class<IR> idResourceClass, final String idToken) {
        mResourceClassToIdTokenMap.put(idResourceClass, idToken);
    }

    public static <IR extends KeyedObjectResource> String getIdToken(Class<IR> idResourceClass) {
        return mResourceClassToIdTokenMap.get(idResourceClass);
    }

    public static <IR extends KeyedObjectResource, IO extends KeyedObject> String getCanonicalUriPath(
            Class<IR> idResourceClass, String id) {
        return UriHelper.formatUriTemplate(getCanonicalUriPathTemplate(idResourceClass),
                new ImmutableMap.Builder<String, String>()
                        .put(getIdToken(idResourceClass), id)
                        .build());
    }

    public static <IR extends KeyedObjectResource, IO extends KeyedObject>
    String getCanonicalUri(Class<IR> idResourceClass, String id) {
        return BaseApplication.getServerUrl() + BaseApplication.getRootUrl() + getCanonicalUriPath(idResourceClass,
                id);
    }

    public static <IR extends KeyedObjectResource> long getIdFromUri(final Class<IR> idObjectClass, final String uri) {
        String uriTemplate = getCanonicalUriPathTemplate(idObjectClass);
        String idToken = getIdToken(idObjectClass);
        return UriHelper.getLongTokenValue(uri, BaseApplication.getRootUrl() + uriTemplate, idToken);
    }

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

    public void putResource(final KO domainObject) {
        try {
            // by default, return 204, since we're not returning any representation. Subclasses that override
            // putResource() can change the response status if needed.
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

            // make sure the client isn't trying to PUT a resource value with an ID that doesn't match the one
            // identified by the request URL.
            if (!domainObject.getKey().toString().equals(mKeyString)) {
                throw new IllegalArgumentException("The ID of the resource in the request body does not match the ID " +
                        "of the resource in the request URL");
            }

            mBiz.update(domainObject);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Delete
    public void deleteResource() {
        try {
            mBiz.delete(mKeyString);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    public String getCanonicalUri(final KeyedObject keyedObject) {
        return getCanonicalUri(this.getClass(), keyedObject.getKeyAsString());
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
        return getIdToken(this.getClass());
    }
}
