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

public abstract class BaseConverter {
    private final String mUriRoot;
    private final String mCanonicalUriPathTemplate;

    protected BaseConverter(final String uriRoot, final String canonicalUriPathTemplate) {
        mUriRoot = uriRoot;
        mCanonicalUriPathTemplate = canonicalUriPathTemplate;
    }

    // this implementation handles DTOs with no tokens in their URI templates.  Adapters for DTOs with tokens in
    // their URI templates will need to override this method and implement the token substitution.
    public String getCanonicalUriPath() {
        // make sure there are no tokens in the template
        if (mCanonicalUriPathTemplate.contains("{"))
            throw new IllegalStateException("this resource's URI template contains tokens which must be filled in with"
                    + " values from a model object.  Use getCanonicalUriPath(PersistentObject) instead.");
        return mCanonicalUriPathTemplate;
    }

    protected String getCanonicalUriTemplate() {
        return mUriRoot + mCanonicalUriPathTemplate;
    }
}
