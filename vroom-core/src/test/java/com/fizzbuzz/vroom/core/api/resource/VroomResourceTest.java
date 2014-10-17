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

import com.fizzbuzz.vroom.core.api.VroomApiModule;
import com.fizzbuzz.vroom.core.api.VroomApiModule.Strict;
import com.fizzbuzz.vroom.core.api.application.VroomApplication;
import com.fizzbuzz.vroom.core.api.resource.VroomResource.CacheKey;
import com.fizzbuzz.vroom.core.api.resource.VroomResource.CachedString;
import com.google.appengine.api.memcache.MemcacheService;
import dagger.Module;
import dagger.Provides;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.engine.resource.AnnotationInfo;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.then;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class VroomResourceTest {
    private TestResource mTestResource;
    private VroomApplication mTestApplication = new TestApplication();
    private MemcacheService mMockMemcacheService = mock(MemcacheService.class);
    private String mTestResourceUrl = "http://test.com/foo";
    private String mTestResourceValue = "a Foo representation";
    private VariantInfo mVariantInfo;


    @Before
    public void setUp() throws Exception {
        mTestApplication.start();

        mTestResource = new TestResource(mTestResourceValue);
        mTestResource.setApplication(mTestApplication);
        Request testRequest = new Request(Method.GET, mTestResourceUrl);
        mTestResource.init(new Context(), testRequest, new Response(testRequest));
        mTestResource.resetDoNotCacheSet(); // clear the static do-not-cache set before each test method

        VroomApplication.registerUrlRoot("http://test.com");
        ResourceRegistry.registerResource(mTestResource.getClass(), "/{token}");

        AnnotationInfo annotationInfo = new AnnotationInfo(TestResource.class, Method.GET, mTestResource.getClass().getMethod("getResource"), "");
        mVariantInfo = new VariantInfo(MediaType.APPLICATION_JSON, annotationInfo);
    }

    @Test
    public void getCanonicalUriPath_throwsWithTokenInUriTemplate() throws Exception {
        // given a registered resource with a token element in its template URI

        // when attempting to get its canonical URI path without providing a token value
        when(mTestResource).getPath();

        // then an IllegalStateException should be thrown
        then(caughtException())
                .isInstanceOf(IllegalStateException.class);
    }
    @Test
    public void isCachedResource_returnsTrueForAnnotatedClass() {
        assertThat(mTestResource.isCachedResource()).isTrue();
    }

    @Test
    public void isCachedResource_returnsFalseForUnannotatedClass() {
        assertThat(new UnannotatedResource().isCachedResource()).isFalse();
    }


    @Test
    public void getCached_addsUncachedRepresentationToMemcache() throws IOException {
        Representation rep = mTestResource.cachedGet(mVariantInfo);
        ArgumentCaptor<CacheKey> keyCaptor = ArgumentCaptor.forClass(CacheKey.class);
        ArgumentCaptor<CachedString> valueCaptor = ArgumentCaptor.forClass(CachedString.class);

        verify(mMockMemcacheService).put(keyCaptor.capture(), valueCaptor.capture());
        assertThat(keyCaptor.getValue().getResourceUrl()).isEqualTo(mTestResourceUrl);
        assertThat(keyCaptor.getValue().getMediaTypeString()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(valueCaptor.getValue().getValue()).isEqualTo(mTestResourceValue);
        assertThat(rep.toString()).isEqualTo(mTestResourceValue);
    }

    @Test
    public void getCached_getsCachedRepresentationFromMemcache() throws IOException {
        // simulate the presence of a matching cache entry
        Mockito.when(mMockMemcacheService.get(any(CacheKey.class))).thenReturn(new CachedString(mTestResourceValue));

        Representation rep = mTestResource.cachedGet(mVariantInfo);

        // verify put() was not called on the memcache service
        verify(mMockMemcacheService, never()).put(any(CacheKey.class), any(CachedString.class));
        assertThat(rep.toString()).isEqualTo(mTestResourceValue);
    }

    @Test
    public void getCached_handlesRepresentationsTooBigToCache() {
        String bigString = randomString(10000000);
        mTestResource.setValue(bigString);
        Representation rep = mTestResource.cachedGet(mVariantInfo);
        assertThat(rep.toString()).isEqualTo(bigString);
    }

    private String randomString( int len )
    {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Cache
    public class TestResource extends VroomResource<Object> {
        String mValue = mTestResourceValue;

        public TestResource(final String value) {
            mValue = value;
        }
        @Override
        @Get("json")
        public Object getResource() {
            return mValue;
        }

        @Override
        public List<Variant> getVariants() {
            List<Variant> result = new ArrayList<>();
            result.add(new Variant(MediaType.APPLICATION_JSON));
            return super.getVariants();
        }

        public void setValue(String value) {
            mValue = value;
        }
    }

    static class UnannotatedResource extends VroomResource<Object> {
    }

    class TestApplication extends VroomApplication {
        @Override
        protected List<Object> getModules() {
            List<Object> modules = super.getModules();
            modules.add(new TestModule());
            return modules;
        }
    }

    @Module(injects={TestApplication.class, TestResource.class}, includes=VroomApiModule.class, overrides=true)
    class TestModule {
        @Provides @Strict
        MemcacheService provideMemcacheService() {
            return mMockMemcacheService;
        }
    }
}
