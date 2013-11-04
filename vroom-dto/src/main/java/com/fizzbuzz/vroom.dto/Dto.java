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

/**
 * A base class for DTOs.
 */
public class Dto {

    private String selfRef;

    // default constructor needed by Jackson to create objects via reflection
    public Dto() {
    }

    protected Dto(final String selfRef) {
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
