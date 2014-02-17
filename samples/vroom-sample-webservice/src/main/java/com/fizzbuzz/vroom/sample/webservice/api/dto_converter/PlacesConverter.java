package com.fizzbuzz.vroom.sample.webservice.api.dto_converter;

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

import com.fizzbuzz.vroom.sample.dto.PlaceDto;
import com.fizzbuzz.vroom.sample.webservice.api.application.MediaTypes;
import com.fizzbuzz.vroom.sample.webservice.domain.Places;
import com.fizzbuzz.vroom.core.api.dto_converter.DomainCollectionConverter;
import com.fizzbuzz.vroom.sample.dto.PlacesDto;
import com.fizzbuzz.vroom.sample.webservice.domain.Place;
import org.restlet.data.MediaType;

public class PlacesConverter
        extends DomainCollectionConverter<PlacesDto, PlaceDto, Places, Place> {

    public PlacesConverter() {
        super(PlacesDto.class,
                Places.class,
                new PlaceConverter(),
                MediaTypes.PlacesMediaTypes.JSON_V1_0_MEDIATYPE,
                MediaType.APPLICATION_JSON);
    }
}