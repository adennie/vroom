package com.fizzbuzz.vroom.extension.googlecloudstorage.api.resource;

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

import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.extension.googlecloudstorage.biz.GcsImageBiz;
import com.fizzbuzz.vroom.extension.googlecloudstorage.domain.IGcsFile;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Redirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GcsImageResource<BIZ extends GcsImageBiz<F, ?>, F extends IGcsFile>
        extends GcsFileResource<BIZ, F> {

    static final String PARAM_SIZE = "size";
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public F getResource() {
        // accept image size constraints from client and call Biz method to generate image serving URL
        Integer size = null;
        try {
            size = getIntegerParamValue(PARAM_SIZE);
        } catch (NumberFormatException e) {
            String msg = "Invalid format for size parameter.  Must be an integer, " +
                    "but value was " + getStringParamValue(PARAM_SIZE);
            mLogger.error(msg);
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, msg, e);
        }

        String servingUrl = getBiz().getServingUrl(new LongKey(getKeyString()), size);
        mLogger.debug("File serving URL is: " + servingUrl);

        // apply client-side redirect from request URL to file serving URL (this will return a status code 303).
        Redirector redirector = new Redirector(getContext(), servingUrl, Redirector.MODE_CLIENT_SEE_OTHER);
        redirector.handle(getRequest(), getResponse());

        // no representation is returned in the response body
        return null;
    }
}
