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

import com.andydennie.vroom.sample.webservice.resource.ImageCommonResource;
import com.andydennie.vroom.core.api.resource.KeyedObjectResource;
import com.andydennie.vroom.sample.webservice.api.application.UriTokens;
import com.andydennie.vroom.sample.webservice.api.application.Uris;
import com.andydennie.vroom.sample.webservice.resource.ImageCommonResource;

public class ImageResource extends ImageCommonResource {

    static public void register() {
        KeyedObjectResource.registerResource(ImageResource.class, Uris.IMAGE_TEMPLATE);
        KeyedObjectResource.registerIdToken(ImageResource.class, UriTokens.IMAGE_ID);
    }
}
