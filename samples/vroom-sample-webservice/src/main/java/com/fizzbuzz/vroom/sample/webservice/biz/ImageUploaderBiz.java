package com.fizzbuzz.vroom.sample.webservice.biz;

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

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;

public class ImageUploaderBiz {

    final static String IMAGE_BUCKET = "vroom-sample-webservice-images";

    public String getImageUploadUrl(final String requestHandler) {
        // method to start blobstore service and generate one-time upload URL to be used in multipart form-data
        // basic multipart form-data defined in image_upload_form.html
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        UploadOptions uploadOptions = UploadOptions.Builder.withGoogleStorageBucketName(IMAGE_BUCKET);
        String uploadUrl = blobstoreService.createUploadUrl(requestHandler, uploadOptions);
        return uploadUrl;
    }
}
