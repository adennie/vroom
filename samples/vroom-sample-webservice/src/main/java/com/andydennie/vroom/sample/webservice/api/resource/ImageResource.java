package com.andydennie.vroom.sample.webservice.api.resource;

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

import com.andydennie.vroom.extension.googlecloudstorage.api.resource.GcsImageResource;
import com.andydennie.vroom.extension.googlecloudstorage.domain.GcsFile;
import com.andydennie.vroom.sample.webservice.biz.ImageBiz;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

public class ImageResource
        extends GcsImageResource<ImageBiz, GcsFile> {

    @Override
    @Get("PNG image | GIF image")
    public GcsFile getResource() {
        try {
            super.getResource();
            addEdgeCachingHeaders();
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return null;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit(new ImageBiz());

        // enable caching of GET responses in the Edge Cache.  Note: this controls caching of the redirect
        // response, not the image itself.  The caching of the image itself is determined by the cache control
        // headers provided by the Image Service or GCS, whichever service actually serves the image file.
        enableEdgeCaching(60 * 60 * 24 * 364); // 364 days
    }
}
