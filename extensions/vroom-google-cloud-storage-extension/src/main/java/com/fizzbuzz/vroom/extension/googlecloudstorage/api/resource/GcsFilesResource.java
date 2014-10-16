package com.fizzbuzz.vroom.extension.googlecloudstorage.api.resource;

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

import com.fizzbuzz.vroom.core.api.resource.KeyedResource;
import com.fizzbuzz.vroom.core.api.resource.ResourceRegistry;
import com.fizzbuzz.vroom.core.api.resource.VroomResource;
import com.fizzbuzz.vroom.core.biz.IFileBiz;
import com.fizzbuzz.vroom.extension.googlecloudstorage.api.util.RepresentationContext;
import com.fizzbuzz.vroom.extension.googlecloudstorage.domain.IGcsFile;
import com.google.common.io.ByteStreams;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class GcsFilesResource<
        F extends IGcsFile,
        B extends IFileBiz<F>>
        extends VroomResource {

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);
    private B mBiz;
    private Class<? extends KeyedResource> mElementResourceClass;

    /**
     *
     * @param rep
     */
    public void postResource(final Representation rep) {
        if (rep == null)
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        else {
            try {
                FileUpload fileUpload = new FileUpload();
                FileItemIterator fileItemIterator = fileUpload.getItemIterator(new RepresentationContext(rep));
                if (fileItemIterator.hasNext()) {
                    FileItemStream fileItemStream = fileItemIterator.next();
                    validateFileItemStream(fileItemStream);
                    String fileName = fileItemStream.getName();
                    byte[] bytes = ByteStreams.toByteArray(fileItemStream.openStream());

                    F file = createFile(fileName);
                    mBiz.create(file, bytes);

                    // we're not going to send a representation of the file back to the client, since they probably
                    // don't want that.  Instead, we'll send them a 201, with the URL to the created file in the
                    // Location header of the response.
                    getResponse().setStatus(Status.SUCCESS_CREATED);
                    // set the Location response header
                    String uri = getElementUri(file);
                    getResponse().setLocationRef(uri);
                    mLogger.info("created new file resource at: {}", uri);
                }
            } catch (FileUploadException e) {
                throw new IllegalArgumentException("caught FileUploadException while attempting to parse " +
                        "multipart form", e);
            } catch (IOException e) {
                throw new IllegalArgumentException("caught IOException while attempting to parse multipart " +
                        "form", e);
            }
        }
    }

    /**
     * Deletes a collection of files
     */
    public void deleteResource() {
        mBiz.deleteAll();
    }

    /**
     * Creates a domain object of generic type F.  Concrete subclasses must override and provide an appropriate
     * implementation.
     *
     * @param fileName
     * @return
     */
    protected abstract F createFile(final String fileName);

    protected String getElementUri(final F element) {
        return ResourceRegistry.getCanonicalUri(mElementResourceClass, element.getKeyAsString());
    }

    protected void doInit(final Class<? extends KeyedResource> elementResourceClass, final B collectionBiz)
            throws ResourceException {
        super.doInit();
        mElementResourceClass = elementResourceClass;
        mBiz = collectionBiz;
    }

    /**
     * Validates a FileItemStream within a multi-part form.  Subclasses may wish to override this method to ensure
     * the file element(s) within the form have valid content types, names, etc., throwing an appropriate exception if
     * an element is not valid.
     *
     * @param fileItemStream
     */
    protected void validateFileItemStream(final FileItemStream fileItemStream) {
    }
}
