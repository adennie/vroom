package com.fizzbuzz.vroom.core.api.resource;

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

import com.fizzbuzz.vroom.core.api.application.VroomApplication;
import com.fizzbuzz.vroom.core.api.util.UriHelper;
import com.fizzbuzz.vroom.core.domain.KeyType;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class ResourceRegistry {
    private static Map<Class<? extends VroomResource>, String> sResourceToPathTemplateMap = new
            HashMap<>();
    private static Map<Class<? extends KeyedResource>, String> sResourceToIdTokenMap = new HashMap<>();

    public static <R extends VroomResource> void registerResource(
            final Class<R> resourceClass, final String pathTemplate) {
        sResourceToPathTemplateMap.put(resourceClass, pathTemplate);
    }

    public static <R extends IVroomResource> String getPathTemplate(Class<R> resourceClass) {
        return sResourceToPathTemplateMap.get(resourceClass);
    }

    public static Map<Class<? extends VroomResource>, String> getPathTemplates() {
        return sResourceToPathTemplateMap;
    }

    public static <KR extends KeyedResource> void registerIdToken(
            final Class<KR> idResourceClass, final String idToken) {
        sResourceToIdTokenMap.put(idResourceClass, idToken);
    }

    public static <KR extends KeyedResource> String getIdToken(Class<KR> idResourceClass) {
        return sResourceToIdTokenMap.get(idResourceClass);
    }
    public static <KR extends KeyedResource> String getPath(
            Class<KR> keyedResourceClass, String id) {
        return UriHelper.formatUriTemplate(getPathTemplate(keyedResourceClass),
                new ImmutableMap.Builder<String, String>()
                        .put(getIdToken(keyedResourceClass), id)
                        .build());
    }
    public static <KR extends KeyedResource>
    String getCanonicalUri(Class<KR> keyedResourceClass, String id) {
        return VroomApplication.getServerUrl() + VroomApplication.getRootUrl() + getPath(keyedResourceClass,
                id);
    }

    public static <KR extends KeyedResource> String getCanonicalUri(Class<KR> keyedResourceClass, KeyType<?> key) {
        return VroomApplication.getServerUrl() + VroomApplication.getRootUrl() + getPath(keyedResourceClass,
                key.toString());
    }


    public static <KR extends KeyedResource> long getIdFromUri(final Class<KR> keyedResourceClass, final String uri) {
        String uriTemplate = getPathTemplate(keyedResourceClass);
        String idToken = getIdToken(keyedResourceClass);
        return UriHelper.getLongTokenValue(uri, VroomApplication.getRootUrl() + uriTemplate, idToken);
    }

    public static <R extends IDomainCollectionResource> String getPath(
            Class<R> collectionResourceClass) {
        return VroomApplication.getServerUrl() + VroomApplication.getRootUrl() + ResourceRegistry.getPathTemplate
                (collectionResourceClass);
    }
}
