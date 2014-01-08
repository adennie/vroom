package com.andydennie.vroom.sample.webservice.resource;

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

import com.andydennie.vroom.core.api.resource.KeyedResource;
import com.andydennie.vroom.core.domain.LongKey;
import com.andydennie.vroom.sample.webservice.biz.ImageBiz;
import com.andydennie.vroom.sample.webservice.domain.Image;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Redirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageCommonResource
        extends KeyedResource<ImageBiz, Image> {

    static final String PARAM_SIZE = "size";
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    @Get("PNG image | GIF image")
    public Image getResource() {
        // accept image size constraints from client and call Biz method to generate image serving URL
        try {
            Long size = getLongParamValue(PARAM_SIZE);
            String servingUrl = getBiz().getServingUrl(new LongKey(getKeyString()),
                    size == null ? null : size.intValue());
            mLogger.debug("Image serving URL is: " + servingUrl);

            // apply client-side redirect from request URL to image serving URL
            Redirector redirector = new Redirector(getContext(), servingUrl, Redirector.MODE_CLIENT_SEE_OTHER);
            redirector.handle(getRequest(), getResponse());
        } catch (NumberFormatException e) {
            String msg = "Invalid format for size parameter.  Must be an integer, " +
                    "but value was " + getStringParamValue(PARAM_SIZE);
            mLogger.error(msg);
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, msg, e);
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return null;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit(new ImageBiz());
    }

}
