package com.andydennie.vroom.dto;

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

public class CollectionDto<DTO extends VroomDto>  {
    private String selfRef;
    private ArrayList<DTO> elements = new ArrayList<DTO>();

    // default constructor needed by Jackson to create objects via reflection
    public CollectionDto() {
    }

    public CollectionDto(final String selfRef, final List<DTO> elements) {
        this.selfRef = selfRef;
        this.elements.addAll(elements);
    }

    public String getSelfRef() {
        return selfRef;
    }
    public void setSelfRef(final String selfRef) {
        this.selfRef = selfRef;
    }

    public void setElements(final ArrayList<DTO> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
    }

    public ArrayList<DTO> getElements() {
        return elements;
    }
}
