package com.fizzbuzz.vroom.sample.webservice.api.resource;

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

import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.extension.googlecloudstorage.api.resource.GcsFilesResource;
import com.fizzbuzz.vroom.extension.googlecloudstorage.domain.GcsFile;
import com.fizzbuzz.vroom.sample.webservice.biz.ImageBiz;
import org.apache.commons.fileupload.FileItemStream;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;

public class ImagesResource extends GcsFilesResource<GcsFile, ImageBiz> {

    @Override
    @Post("multipart/form-data")
    public void postResource(final Representation rep) {
        try {
            super.postResource(rep);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Override
    @Delete
    public void deleteResource() {
        try {
            super.deleteResource();
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Override
    protected GcsFile createFile(final String fileName) {
        return new GcsFile(new LongKey((Long) null), null, fileName);
    }

    @Override
    protected void doInit() {
        super.doInit(ImageResource.class, new ImageBiz());
    }

    @Override
    protected void validateFileItemStream(final FileItemStream fileItemStream) {
        super.validateFileItemStream(fileItemStream);
        String contentType = fileItemStream.getContentType();
        if (!(contentType.equals(MediaType.IMAGE_PNG.getName())) || contentType.equals(MediaType.IMAGE_GIF.getName())) {
            throw new IllegalArgumentException("image content type must be either image/png or image/gif");
        }

    }
}
