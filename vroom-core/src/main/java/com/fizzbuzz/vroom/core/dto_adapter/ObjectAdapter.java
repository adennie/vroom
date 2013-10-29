package com.fizzbuzz.vroom.core.dto_adapter;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

import com.fizzbuzz.vroom.core.resource.UriHelper;
import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.dto.ObjectDto;


public abstract class ObjectAdapter<DTO extends ObjectDto, DO extends DomainObject> extends BaseAdapter {
    private final String mIdToken;

    public ObjectAdapter(final String uriTemplate, final String idToken) {
        super(uriTemplate);
        mIdToken = idToken;
    }

    abstract public DTO toDto(final DO modelObject);

    abstract public DO toDomain(final DTO dto);

    /**
     * Returns the ID of the domain object represented by a DTO by parsing a token value from its URI
     *
     * @param dto a DTO representing a domain object
     * @return the domain object's ID
     */
    public long getId(final ObjectDto dto) {
        return getId(dto.getUri());
    }

    /**
     * Returns the ID of the domain object by parsing a token value from a DTO's URI
     *
     * @param uri a URI representing a domain object
     * @return the domain object's ID
     */
    public long getId(final String uri) {
        // normally the "uri" field in the DTO must be formatted correctly, but in the special case of creating a new
        // object, it will be null.  In that case, we'll set the ID of the domain object to -1 temporarily.
        return uri == null ? -1L : UriHelper.getLongTokenValue(uri, getUriTemplate(), mIdToken);
    }

    public abstract String getCanonicalUri(DO domainObject);

    public String getIdToken() {
        return mIdToken;
    }
}
