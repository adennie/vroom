package com.fizzbuzz.vroom.sample.dto;

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

import com.fizzbuzz.vroom.dto.SimpleDto;

/**
 * A data transfer object representing a place definition
 */
public class PlaceDto extends SimpleDto {
    private String name;
    private LocationProperty location;

    //TODO add references to contacts? e.g. people to contact at this place?


    // default constructor needed by Jackson to create objects via reflection
    public PlaceDto() {
    }

    public PlaceDto(final String selfRef, final String name, final LocationProperty location) {
        super(selfRef);
        this.name = name;
        this.location = location;
    }

    public LocationProperty getLocation() {
        return location;
    }

    public void setLocation(final LocationProperty location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
