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

import com.fizzbuzz.vroom.core.domain.DomainCollection;
import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.resource.DomainCollectionResource;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.fizzbuzz.vroom.dto.CollectionDto;
import com.fizzbuzz.vroom.dto.Dto;

import java.util.ArrayList;
import java.util.List;

public abstract class DomainCollectionConverter<
        DTC extends CollectionDto<DTO>,
        DTO extends Dto,
        DC extends DomainCollection<DO>,
        DO extends DomainObject> implements BaseConverter {
    private DomainObjectConverter<DTO, DO> mElementConverter;
    private Class<DTC> mDtcClass;

    public DomainCollectionConverter(final Class<DTC> dtcClass,
                                     final DomainObjectConverter<DTO, DO> elementConverter) {
        mElementConverter = elementConverter;
        mDtcClass = dtcClass;
    }

    public CollectionDto<DTO> toDto(DomainCollectionResource<DC, DO> resource, final DomainCollection<DO> domainCollection) {
        // first convert the DO objects into DTO objects
        List<DTO> dtos = new ArrayList<DTO>();
        for (DO domainObject : domainCollection) {
            dtos.add(mElementConverter.toDto(domainObject));
        }

        // get the URI of the collection resource
        String collectionSelfRef = getCanonicalUri(resource);

        // now create/return the CollectionDto
        CollectionDto<DTO> result = Reflections.newInstance(mDtcClass, String.class, List.class, collectionSelfRef, dtos);
        return result;
    }

    protected String getCanonicalUri(DomainCollectionResource<DC, DO> resource) {
        return DomainCollectionResource.getCanonicalUri(resource.getClass());
    }
}

