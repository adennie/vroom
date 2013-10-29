package com.fizzbuzz.vroom.core.dto_adapter;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public abstract class BaseAdapter {
    private final String mUriTemplate;

    protected BaseAdapter(String uriTemplate) {
        mUriTemplate = uriTemplate;
    }

    protected String getUriTemplate() {
        return mUriTemplate;
    }

    // this implementation handles DTOs with no tokens in their URI templates.  Adapters for DTOs with tokens in
    // their URI templates will need to override this method and implement the token substitution.
    protected String getCanonicalUri() {
        // make sure there are no tokens in the template
        if (getUriTemplate().contains("{"))
            throw new IllegalStateException("this resource's URI template contains tokens which must be filled in with"
                    + " values from a model object.  Use getCanonicalUri(DomainObject) instead.");
        return getUriTemplate();
    }
}
