package com.fizzbuzz.vroom.dto;

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

public class SimpleDto implements VroomDto {
    private String selfRef;

    // default constructor needed by Jackson to create objects via reflection
    public SimpleDto() {
    }

    protected SimpleDto(final String selfRef) {
        this.selfRef = selfRef;
    }

    @Override
    public String getSelfRef() {
        return selfRef;
    }

    @Override
    public void setSelfRef(final String selfRef) {
        this.selfRef = selfRef;
    }


    @Override
    public void validate(final HttpMethod method) {
        // selfRef must be null for a POST request
        if (method == HttpMethod.POST && selfRef != null) {
            throw new IllegalArgumentException("selfRef field must be null in a POST request.  The value will be " +
                    "assigned server-side at creation time.");
        }

        // selfRef must be non-null for a PUT request
        if (method == HttpMethod.PUT && selfRef == null) {
            throw new IllegalArgumentException("selfRef field must be non-null in a PUT request.  The value should " +
                    "be the one assigned server-side at creation time.");
        }
    }
}
