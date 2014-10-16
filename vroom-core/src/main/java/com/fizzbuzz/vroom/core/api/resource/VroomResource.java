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

import com.fizzbuzz.vroom.core.api.VroomApiModule.Strict;
import com.fizzbuzz.vroom.core.api.application.VroomApplication;
import com.fizzbuzz.vroom.core.exception.ConflictException;
import com.fizzbuzz.vroom.core.exception.InvalidResourceUriException;
import com.fizzbuzz.vroom.core.exception.MemcacheValueTooLargeException;
import com.fizzbuzz.vroom.core.exception.NotFoundException;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.fizzbuzz.vroom.core.util.Strings;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import dagger.Lazy;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Options;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class VroomResource<R> extends ServerResource implements IVroomResource<R> {

    private static final double MEMCACHE_VALUE_SIZE_LIMIT = Math.pow(2, 20); // 1 MB
    // limit to 364 days.  See https://groups.google.com/d/msg/google-appengine/6xAV2Q5x8AU/QI26C0ofvhwJ
    private static final int EDGE_CACHE_MAX_AGE = 60 * 60 * 24 * 364;
    private static Set<CacheKey> mDoNotCache = Collections.synchronizedSet(new HashSet<CacheKey>());
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);
    private final List<CacheDirective> mEdgeCacheDirectives = new ArrayList<>();
    @Inject @Strict Lazy<MemcacheService> mLazyMemcacheService;
    private boolean mEdgeCachingEnabled = false;

    public boolean isEdgeCachingEnabled() {
        return mEdgeCachingEnabled;
    }

    public void enableEdgeCaching(final int maxAge) {

        if (maxAge > EDGE_CACHE_MAX_AGE)
            throw new IllegalArgumentException("For Edge Caching, max-age should be no more than 364 days");

        mEdgeCachingEnabled = true;
        mEdgeCacheDirectives.clear();
        mEdgeCacheDirectives.add(CacheDirective.publicInfo());
        mEdgeCacheDirectives.add(CacheDirective.maxAge(maxAge));
    }

    public void disableEdgeCaching() {
        mEdgeCachingEnabled = false;
    }

    @Override
    public R getResource() {
        return null;
    }

    @Override
    public void putResource(final R object) {
    }

    @Override
    public void deleteResource() {
    }

    @Options
    public void doOptions() {
        // setting the Allow response header is done automatically by Restlet as long as this @Options-annotated
        // method exists
    }

    // this default implementation just returns the path template.  If the template contains any tokens, the
    // subclass should override this method and perform the token substitution.
    public String getPath() {
        String pathTemplate = getPathTemplate();
        if (pathTemplate.contains("{"))
            throw new IllegalStateException("this resource's URI template contains tokens which must be substituted " +
                "with values.");
        return pathTemplate;

    }

    @Override
    protected Representation get(Variant variant) throws ResourceException {
        Representation result;

        if (isCachedResource()) {
            result = cachedGet(variant);
        } else {
            result = super.get(variant);
        }
        return result;
    }

    protected void addEdgeCachingHeaders() {
        // See https://groups.google.com/d/msg/google-appengine/6xAV2Q5x8AU/QI26C0ofvhwJ
        getResponse().setCacheDirectives(mEdgeCacheDirectives);
        addPragmaResponseHeader("Public");
    }

    protected void doCatch(final RuntimeException e) {
        Class<?> exceptionClass = e.getClass();
        if (IllegalArgumentException.class.isAssignableFrom(exceptionClass)
            || InvalidResourceUriException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 400
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage(), e);
        }
        if (NotFoundException.class.isAssignableFrom(exceptionClass)
            || IndexOutOfBoundsException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 404
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getMessage(), e);
        }
        if (UnsupportedOperationException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 405
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, e.getMessage(), e);
        }
        if (ConflictException.class.isAssignableFrom(exceptionClass)
            || IllegalStateException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 409
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_CONFLICT, e.getMessage(), e);
        }

        throw e;
    }

    protected long getLongTokenValue(final String token) {

        String s = getTokenValue(token);

        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "invalid " + token + " URL component: " + s);
        }
    }

    protected String getTokenValue(final String token) {
        String result = (String) getRequestAttributes().get(token);

        if (result == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "missing " + token + " URL component: " +
                getRequestUrl());
        }

        return result;
    }

    protected Long getLongParamValue(final String paramName) {
        String paramAsString;
        Map<String, String> params = getQuery().getValuesMap();
        paramAsString = params.get(paramName);

        if (paramAsString == null)
            return null;
        return Long.parseLong(paramAsString);
    }

    protected Integer getIntegerParamValue(final String paramName) {
        String paramAsString;
        Map<String, String> params = getQuery().getValuesMap();
        paramAsString = params.get(paramName);

        if (paramAsString == null)
            return null;
        return Integer.parseInt(paramAsString);
    }

    protected String getStringParamValue(final String paramName) {
        Map<String, String> params = getQuery().getValuesMap();
        return params.get(paramName);

    }

    protected String getPathTemplate() {
        return ResourceRegistry.getPathTemplate(this.getClass());
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();

        // inject "this" from the object graph
        getApplication().getObjectGraph().inject(this);
    }

    public VroomApplication getApplication() {
        return (VroomApplication) super.getApplication();
    }

    protected void addPragmaResponseHeader(final String value) {
        addResponseHeader(HeaderConstants.HEADER_PRAGMA, value);
    }

    protected void addResponseHeader(final String header, final String value) {

        Response response = getResponse();
        Series<Header> responseHeaders = (Series<Header>)
            response.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
        if (responseHeaders == null) {
            responseHeaders = new Series(Header.class);
            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                responseHeaders);
        }
        responseHeaders.add(new Header(header, value));
    }

    protected String getRequestUrl() {
        return getRequest().getResourceRef().toString();
    }

    Representation cachedGet(Variant variant) {
        Representation result = null;

        // see if there's a cached representation for this variant
        // the values in the cache are keyed by a combination of resource URL and media type
        MemcacheService memcacheService = mLazyMemcacheService.get();
        CacheKey cacheKey = new CacheKey(getRequestUrl(), variant.getMediaType());
        CachedString cachedString = (CachedString) memcacheService.get(cacheKey);

        if (cachedString != null) {
            // cache hit.  Create a StringRepresentation from the cached string and return it.
            mLogger.debug("VroomResource.get: found representation in memcache for url={}", cacheKey.getResourceUrl());
            try {
                result = new StringRepresentation(cachedString.getValue(), variant.getMediaType());
            } catch (IOException e) {
                mLogger.warn("VroomResource.get: failed to create StringRepresentation from cached value", e);
            }
        }

        if (result == null) {
            // cache miss, or exception attempting to use cached value.  First, build the representation from scratch
            Representation originalRep = super.get(variant);

            // if the cache key is in the "do-not-cache" set, just return the representation we have, otherwise
            // attempt to add it to the cache.
            if (mDoNotCache.contains(cacheKey)) {
                result = originalRep;
            } else {
                // in the interest of performance, we'd like to cache a value that is close to what goes across the
                // wire, so we'll convert the representation we have into a String.
                String repAsString = repToString(originalRep);
                if (repAsString.length() > 0) {
                    addRepToCache(cacheKey, repAsString);
                }

                // return a StringRepresentation (rather than the original representation) since we already
                // went to the effort of string-ifying it.  No point in having Restlet run through that conversion
                // again.
                result = new StringRepresentation(repAsString, variant.getMediaType());
            }
        }
        return result;
    }

    boolean isCachedResource() {
        // look for a @Cache annotation on the resource class or any of its base classes
        Collection<Cache> cacheAnnotations = Reflections.getClassAnnotations(getClass(), VroomResource.class,
            Cache.class);
        return (!cacheAnnotations.isEmpty());
    }

    void resetDoNotCacheSet() {
        mDoNotCache.clear();
    }

    private void addRepToCache(CacheKey cacheKey, String repAsString) {
        try {
            CachedString cachedString = new CachedString(repAsString);
            MemcacheService memcacheService = mLazyMemcacheService.get();
            memcacheService.put(cacheKey, cachedString);
            mLogger.info("VroomResource.get: added representation to memcache for url={}", cacheKey.getResourceUrl());
        } catch (MemcacheValueTooLargeException e) {
            mLogger.warn("VroomResource.get: @Cache-annotated resource too big for memcache (size={}): {}",
                Long.toString(repAsString.length()), cacheKey.getResourceUrl());
            // remember which resources didn't fit in memcache, and don't try to cache them in the future.
            mDoNotCache.add(cacheKey);
        } catch (IOException e) {
            mLogger.warn("VroomResource.get: Representation of @Cache-annotated resource could not be " +
                "converted to CachedString, url={}", cacheKey.getResourceUrl(), e);
        } catch (IllegalArgumentException | MemcacheServiceException e) {
            mLogger.warn("VroomResource.get: failed to add representation of @Cache-annotated resource to" +
                " memcache, url={}", cacheKey.getResourceUrl(), e);
        }
    }

    private String repToString(Representation rep) {
        String result = null;
        StringWriter stringWriter = new StringWriter();
        try {
            rep.write(stringWriter);
            result = stringWriter.toString();
        } catch (IOException e) {
            mLogger.error("VroomResource.get: error when writing original representation to StringWriter", e);
        }
        return result; // will be null if exception caught above
    }

    static class CacheKey implements Serializable {
        private static final long serialVersionUID = -4157783724766782680L;
        private final String mResourceUrl;
        private final String mMediaTypeString;

        CacheKey(final String resourceUrl, final MediaType mediaType) {
            mResourceUrl = checkNotNull(resourceUrl, "resourceUrl must not be null");
            checkNotNull(mediaType, "mediaType must not be null");
            mMediaTypeString = mediaType.toString();
        }

        CacheKey(final String resourceUrl) {
            mResourceUrl = resourceUrl;
            mMediaTypeString = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (!mMediaTypeString.equals(cacheKey.mMediaTypeString)) return false;
            if (!mResourceUrl.equals(cacheKey.mResourceUrl)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mResourceUrl.hashCode();
            result = 31 * result + mMediaTypeString.hashCode();
            return result;
        }

        String getResourceUrl() {
            return mResourceUrl;
        }

        String getMediaTypeString() {
            return mMediaTypeString;
        }
    }

    static class CachedString implements Serializable {
        private boolean mIsCompressed;
        private byte[] mValue;


        CachedString(final String s) throws IOException {
            mValue = s.getBytes();
            if (mValue.length > MEMCACHE_VALUE_SIZE_LIMIT) {
                mValue = Strings.compress(s);
                if (mValue.length > MEMCACHE_VALUE_SIZE_LIMIT)
                    throw new MemcacheValueTooLargeException();
                mIsCompressed = true;
            }
        }

        String getValue() throws IOException {
            String result = new String(mValue);
            if (mIsCompressed) {
                result = Strings.decompress(mValue);
            }
            return result;
        }
    }
}
