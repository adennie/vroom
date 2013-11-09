package com.fizzbuzz.vroom.core.dto_converter;

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

import com.fizzbuzz.vroom.core.domain.IdObject;
import com.fizzbuzz.vroom.core.resource.IdObjectResource;
import com.fizzbuzz.vroom.dto.Dto;
import org.restlet.data.MediaType;

public abstract class IdObjectConverter<DTO extends Dto, IO extends IdObject> extends ObjectConverter<DTO, IO> {

    final Class<? extends IdObjectResource> mResourceClass;

    public IdObjectConverter(final Class<? extends IdObjectResource> resourceClass, final Class<DTO> dtoClass, final Class<IO> idObjectClass, MediaType... supportedMediaTypes) {
        super(dtoClass, idObjectClass, supportedMediaTypes);
        mResourceClass = resourceClass;
    }

    protected String getCanonicalUri(IO idObject) {
        return IdObjectResource.getCanonicalUri(mResourceClass, idObject.getId());
    }

    protected long getIdFromDto(DTO dto) {
        if (dto.getSelfRef() == null)
            return -1;
        else
            return IdObjectResource.getIdFromUri(mResourceClass, dto.getSelfRef());
    }
}
