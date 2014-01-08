package com.andydennie.vroom.sample.dto;

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

import com.andydennie.vroom.dto.SimpleCollectionDto;

import java.util.List;

/**
 * A data transfer object representing a collection of places.
 */

public class PlacesDto extends SimpleCollectionDto<PlaceDto> {

    // default constructor needed by Jackson to create objects via reflection
    public PlacesDto() {
    }

    public PlacesDto(final String selfRef, final List<PlaceDto> elements) {
        super(selfRef, elements);
    }
}
