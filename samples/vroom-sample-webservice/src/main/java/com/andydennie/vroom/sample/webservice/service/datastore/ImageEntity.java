package com.andydennie.vroom.sample.webservice.service.datastore;

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

import com.andydennie.vroom.extension.googlecloudstorage.domain.GcsFile;
import com.andydennie.vroom.extension.googlecloudstorage.service.datastore.GcsEntity;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.common.io.Files;

import java.util.HashMap;
import java.util.Map;

public class ImageEntity extends GcsEntity<GcsFile, ImageDao> {
    private static String BUCKET_NAME = "vroom-sample-images";
    private static Map<String, String> MEDIA_TYPES;

    static {
        MEDIA_TYPES = new HashMap<>();
        MEDIA_TYPES.put("png", "image/png");
        MEDIA_TYPES.put("gif", "image/gif");
        MEDIA_TYPES.put("jpg", "image/jpg");
    }

    public ImageEntity() {
        super(GcsFile.class, ImageDao.class);
    }

    @Override
    public void create(final GcsFile file, final byte[] content) {
        String extension = Files.getFileExtension(file.getFileName()).toLowerCase();
        if (extension.isEmpty())
            throw new IllegalArgumentException("ImageEntity.create: the file extension for file \""
                    + file.getFileName() + "\" is missing");

        String mediaType = MEDIA_TYPES.get(extension);
        if (mediaType == null)
            throw new IllegalArgumentException("The file extension for file \"" +
                    file.getFileName() + "\" does not correspond to a supported image type");

        // build the GcsFileOptions, specifying the media type and cache control metadata
        GcsFileOptions gcsFileOptions = (new GcsFileOptions.Builder())
                .mimeType(mediaType)
                .cacheControl("public, max-age=31449600")
                .build();

        // assign the bucket name into the domain object
        file.setBucketName(BUCKET_NAME);
        super.create(file, gcsFileOptions, content);
    }
}
