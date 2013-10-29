package com.fizzbuzz.vroom.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2013 Fitivity Inc.
 */

public class CollectionDto<DTO extends ObjectDto> extends ArrayList<DTO> {
    private String uri;

    // default constructor needed by Jackson to create objects via reflection
    public CollectionDto() {
    }

    public CollectionDto(final String uri, final List<DTO> elements) {
        this.uri = uri;
        addAll(elements);
    }

    public String getUri() {
        return uri;
    }
    public void setUri(final String uri) {
        this.uri = uri;
    }
}
