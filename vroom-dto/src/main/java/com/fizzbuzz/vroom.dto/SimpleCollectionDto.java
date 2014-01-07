package com.fizzbuzz.vroom.dto;

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

import java.util.ArrayList;
import java.util.List;

public class SimpleCollectionDto<DTO extends VroomDto> implements VroomCollectionDto<DTO> {
    private String selfRef;
    private List<DTO> elements = new ArrayList<DTO>();

    // default constructor needed by Jackson to create objects via reflection
    public SimpleCollectionDto() {
    }

    public SimpleCollectionDto(final String selfRef, final List<DTO> elements) {
        this.selfRef = selfRef;
        this.elements.addAll(elements);
    }

    @Override
    public String getSelfRef() {
        return selfRef;
    }

    @Override
    public void setSelfRef(final String selfRef) {
        this.selfRef = selfRef;
    }

    public List<DTO> getElements() {
        return elements;
    }

    public void setElements(final List<DTO> elements) {
        this.elements = elements;
    }
}
