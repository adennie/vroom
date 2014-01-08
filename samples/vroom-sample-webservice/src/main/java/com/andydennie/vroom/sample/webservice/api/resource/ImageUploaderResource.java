package com.andydennie.vroom.sample.webservice.api.resource;

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

import com.andydennie.vroom.core.api.application.VroomApplication;
import com.andydennie.vroom.core.api.resource.ResourceRegistry;
import com.andydennie.vroom.core.api.resource.VroomResource;
import com.andydennie.vroom.sample.webservice.biz.ImageUploaderBiz;
import org.restlet.resource.Get;

public class ImageUploaderResource extends VroomResource {
    @Get("txt")
    // creates a URL for one-time uploading to the blobstore using web-service URI templates
    public String getResource() {
        String redirectUrl = VroomApplication.getRootUrl() + ResourceRegistry.getPathTemplate(ImagesResource.class);
        return new ImageUploaderBiz().getImageUploadUrl(redirectUrl);
    }
}

