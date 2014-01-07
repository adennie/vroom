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
 * The standard interface for Vroom DTOs.
 */
public interface VroomDto {

    public String getSelfRef();

    public void setSelfRef(final String selfRef);

    /**
     * Validates the state of the DTO.  This method is intended for use in performing first-pass validation on DTOs
     * created by a client, and can be invoked client-side or server-side.  Examples include:
     * - verifying that values representing enumerations are one of the allowed values
     * - verifying that strings which must conform to a regular expression do so
     * - verifying that numbers are in a permitted range
     * - verifying that collections which must be non-empty are non-empty
     * - verifying that read-only fields which are assigned server-side at creation time are non-null for a PUT
     * request, but null for a POST request.
     */
    public void validate(final HttpMethod method);
}
