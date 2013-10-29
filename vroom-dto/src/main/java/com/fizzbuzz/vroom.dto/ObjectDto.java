package com.fizzbuzz.vroom.dto;

/**
 * Copyright (c) 2013 Fitivity Inc.
 */

public class ObjectDto {

    private String uri;

    // default constructor needed by Jackson to create objects via reflection
    public ObjectDto() {
    }

    protected ObjectDto(final String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    /**
     * Validates the state of the DTO.  This method is intended for use in performing first-pass validation on DTOs
     * received from a client.  Validation in this context should be limited to that which can be performed
     * without referring to domain objects or the persistence layer.  Examples include:
     * - verifying that values representing enumerations are one of the allowed values
     * - verifying that strings which must conform to a regular expression do so
     * - verifying that numbers are in a permitted range
     * - verifying that collections which must be non-empty are non-empty
     */
    public void validate() {
    }
}
