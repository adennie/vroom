package com.fizzbuzz.vroom.sample.webservice.api.resource;

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

import com.fizzbuzz.vroom.core.api.application.VroomApplication;
import com.fizzbuzz.vroom.core.api.resource.KeyedObjectResource;
import com.fizzbuzz.vroom.core.api.resource.VroomResource;
import com.fizzbuzz.vroom.sample.webservice.api.application.Uris;
import com.fizzbuzz.vroom.sample.webservice.biz.ImageUploaderBiz;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.resource.Get;

import java.util.List;

public class ImageUploaderResource extends VroomResource {

    //TODO: If we determine is unnecessary, remove

    static public void register(List<ConverterHelper> converterHelpers) {
        KeyedObjectResource.registerResource(ImageUploaderResource.class, Uris.IMAGE_UPLOADER);
    }

    @Get("txt")
    // creates a URL for one-time uploading to the blobstore using web-service URI templates
    public String getResource() {
        String redirectUrl = VroomApplication.getRootUrl() + getCanonicalUriPathTemplate(ImagesResource.class);
        return new ImageUploaderBiz().getImageUploadUrl(redirectUrl);
    }
}

