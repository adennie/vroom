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

import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.util.Reflections;
import com.fizzbuzz.vroom.dto.CollectionDto;
import com.fizzbuzz.vroom.dto.Dto;

import java.util.ArrayList;
import java.util.List;

public abstract class DomainCollectionConverter<
        DTC extends CollectionDto<DTO>,
        DTO extends Dto,
        DO extends DomainObject> implements BaseConverter {

    private DomainObjectConverter<DTO, DO> mElementConverter;
    private Class<DTC> mDtcClass;

    public DomainCollectionConverter(final Class<DTC> dtcClass, final DomainObjectConverter<DTO, DO> elementConverter) {
        mElementConverter = elementConverter;
        mDtcClass = dtcClass;
    }

    public DTC toDto(final List<DO> domainCollection, final String collectionSelfRef) {
        // first convert the DO objects into DTO objects
        List<DTO> dtos = new ArrayList<DTO>();
        for (DO domainObject : domainCollection) {
            dtos.add(mElementConverter.toDto(domainObject));
        }

        // now create/return the CollectionDto
        DTC result = Reflections.newInstance(mDtcClass, String.class, List.class, collectionSelfRef, dtos);
        return result;
    }
}

