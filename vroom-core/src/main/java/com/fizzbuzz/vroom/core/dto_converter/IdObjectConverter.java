package com.fizzbuzz.vroom.core.dto_converter;

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

import com.fizzbuzz.vroom.core.domain.IdObject;
import com.fizzbuzz.vroom.core.resource.UriHelper;
import com.fizzbuzz.vroom.dto.Dto;


public abstract class IdObjectConverter<DTO extends Dto, IO extends IdObject> extends ObjectConverter<DTO, IO> {
    private final String mIdToken;

    public IdObjectConverter(final String uriRoot, final String canonicalUriPathTemplate, final String idToken) {
        super(uriRoot, canonicalUriPathTemplate);
        mIdToken = idToken;
    }

    /**
     * Returns the ID of a resource represented by a DTO by parsing a token value from the URI stored in its
     * "selfRef" property
     *
     * @param dto a DTO representing a resource
     * @return the domain object's ID
     */
    public long getIdFromDto(final Dto dto) {
        return getId(dto.getSelfRef());
    }

    /**
     * Returns the ID of a resource by parsing a token value from a its URI
     *
     * @param uri a canonical URI for a resource
     * @return the domain object's ID
     */
    public long getId(final String uri) {
        // normally the "selfRef" field in the DTO must be formatted correctly, but in the special case of creating a
        // new resource, it will be null.  In that case, we'll set the ID of the domain object to -1 temporarily.
        return uri == null ? -1L : UriHelper.getLongTokenValue(uri, getCanonicalUriTemplate(), mIdToken);
    }

    public String getIdToken() {
        return mIdToken;
    }
}
