package com.fizzbuzz.vroom.extension.googlecloudstorage.service.gcs;

/*
 * Copyright (c) 2014 Fizz Buzz LLC
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

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

public class GcsImageObject extends GcsFileObject {

    public GcsImageObject(final String bucketName, final String fileName) {
        super(bucketName, fileName);
    }

    public String getServingUrl(final Integer size) {
        // if no size was specified, just serve directly from GCS; no need to involve the Images Service
        if (size == null)
            return getServingUrl();

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withGoogleStorageFileName(getFilePath());
        if (size != null)
            servingUrlOptions.imageSize(size);

        String result = imagesService.getServingUrl(servingUrlOptions);
        return result;
    }
}
