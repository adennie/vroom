package com.andydennie.vroom.extension.googlecloudstorage.api.resource;

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

import com.andydennie.vroom.core.api.resource.KeyedResource;
import com.andydennie.vroom.core.domain.LongKey;
import com.andydennie.vroom.extension.googlecloudstorage.biz.GcsFileBiz;
import com.andydennie.vroom.extension.googlecloudstorage.domain.IGcsFile;
import org.restlet.routing.Redirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GcsFileResource<
        B extends GcsFileBiz<F, ?>,
        F extends IGcsFile>
        extends KeyedResource<LongKey, B, F> {

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public F getResource() {
        String servingUrl = getBiz().getServingUrl(new LongKey(getKeyString()));
        mLogger.debug("File serving URL is: " + servingUrl);

        // apply client-side redirect from request URL to file serving URL (this will return a status code 303).
        Redirector redirector = new Redirector(getContext(), servingUrl, Redirector.MODE_CLIENT_SEE_OTHER);
        redirector.handle(getRequest(), getResponse());

        // no representation is returned in the response body
        return null;
    }
}
