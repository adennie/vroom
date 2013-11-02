package com.fizzbuzz.vroom.dto;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class ObjectDto {

    private String selfRef;

    // default constructor needed by Jackson to create objects via reflection
    public ObjectDto() {
    }

    protected ObjectDto(final String selfRef) {
        this.selfRef = selfRef;
    }

    public String getSelfRef() {
        return selfRef;
    }

    public void setSelfRef(final String selfRef) {
        this.selfRef = selfRef;
    }

    /**
     * Validates the state of the DTO.  This method is intended for use in performing first-pass validation on DTOs
     * received from a client.  Examples include:
     * - verifying that values representing enumerations are one of the allowed values
     * - verifying that strings which must conform to a regular expression do so
     * - verifying that numbers are in a permitted range
     * - verifying that collections which must be non-empty are non-empty
     */
    public void validate() {
    }

    /**
     * Validates the state of the DTO as it relates to its required state when supplied in a PUT request.  Examples
     * include:
     * - verifying that fields which are assigned server-side at creation time are non-null
     */
    public void validatePost() {
        validate();

        if (selfRef != null) {
            throw new IllegalArgumentException("selfRef field must be null in a POST request.  The value will be " +
                    "assigned server-side at creation time.");
        }
    }

    /**
     * Validates the state of the DTO as it relates to its required state when supplied in a POST request.  Examples
     * include:
     * - verifying that fields which are assigned server-side at creation time are null
     */
    public void validatePut() {
        validate();

        if (selfRef == null) {
            throw new IllegalArgumentException("selfRef field must be non-null in a POST request.  The value should " +
                    "be the one assigned server-side at creation time.");
        }
    }
}
