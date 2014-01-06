package com.fizzbuzz.vroom.sample.dto;

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

import com.fizzbuzz.vroom.dto.CollectionDto;

import java.util.List;

/**
 * A data transfer object representing a collection of places.
 */

public class UsersDto extends CollectionDto<UserDto> {

    // default constructor needed by Jackson to create objects via reflection
    public UsersDto() {
    }

    public UsersDto(final String selfRef, final List<UserDto> elements) {
        super(selfRef, elements);
    }
}
