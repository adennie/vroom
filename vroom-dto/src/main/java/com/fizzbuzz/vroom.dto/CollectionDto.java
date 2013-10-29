package com.fizzbuzz.vroom.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class CollectionDto<DTO extends ObjectDto> extends ArrayList<DTO> {
    private String selfRef;

    // default constructor needed by Jackson to create objects via reflection
    public CollectionDto() {
    }

    public CollectionDto(final String selfRef, final List<DTO> elements) {
        this.selfRef = selfRef;
        addAll(elements);
    }

    public String getSelfRef() {
        return selfRef;
    }
    public void setSelfRef(final String selfRef) {
        this.selfRef = selfRef;
    }
}
