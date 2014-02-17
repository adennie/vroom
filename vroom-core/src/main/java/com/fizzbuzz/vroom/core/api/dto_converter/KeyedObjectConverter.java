package com.fizzbuzz.vroom.core.api.dto_converter;

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

import com.fizzbuzz.vroom.core.api.resource.KeyedResource;
import com.fizzbuzz.vroom.core.api.resource.ResourceRegistry;
import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.dto.VroomDto;
import org.restlet.data.MediaType;

public abstract class KeyedObjectConverter<DTO extends VroomDto, KO extends KeyedObject> extends VroomConverter<DTO, KO> {

    final Class<? extends KeyedResource> mResourceClass;

    public KeyedObjectConverter(final Class<? extends KeyedResource> resourceClass,
                                final Class<DTO> dtoClass,
                                final Class<KO> idObjectClass,
                                MediaType... supportedMediaTypes) {
        super(dtoClass, idObjectClass, supportedMediaTypes);
        mResourceClass = resourceClass;
    }

    protected String getCanonicalUri(KO keyedObject) {
        return ResourceRegistry.getCanonicalUri(mResourceClass, keyedObject.getKeyAsString());
    }

    protected Long getIdFromDto(DTO dto) {
        if (dto.getSelfRef() == null)
            return null;
        else
            return ResourceRegistry.getIdFromUri(mResourceClass, dto.getSelfRef());
    }
}
