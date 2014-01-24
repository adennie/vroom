package com.andydennie.vroom.extension.googlecloudstorage.service.gcs;

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

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class GcsFileObject {

    private static String GCS_PATH_ROOT = "/gs/";
    private static String GCS_URL_ROOT = "http://storage.googleapis.com/";
    private GcsFilename mGcsFileName;
    private GcsFileOptions mGcsFileOptions;


    public GcsFileObject(final String bucketName, final String fileName) {
        this(bucketName, fileName, new GcsFileOptions.Builder().build());
    }

    public GcsFileObject(final String bucketName, final String fileName, final GcsAcl gcsAcl) {
        this(bucketName, fileName, new GcsFileOptions.Builder().acl(gcsAcl.get()).build());
    }

    public GcsFileObject(final String bucketName, final String fileName, final GcsFileOptions gcsFileOptions) {
        mGcsFileName = new GcsFilename(bucketName, fileName);
        mGcsFileOptions = gcsFileOptions;
    }

    public void write(final byte[] data) throws IOException {
        GcsOutputChannel output = GcsServiceFactory.createGcsService().createOrReplace(mGcsFileName, mGcsFileOptions);
        output.write(ByteBuffer.wrap(data));
        output.close();
    }

    public void save() throws IOException {
        GcsOutputChannel output = GcsServiceFactory.createGcsService().createOrReplace(mGcsFileName, mGcsFileOptions);
        output.close();
    }

    public void delete() throws IOException {
        GcsServiceFactory.createGcsService().delete(mGcsFileName);
    }

    public String getMimeType() throws IOException {
        return GcsServiceFactory.createGcsService().getMetadata(mGcsFileName).getOptions().getMimeType();
    }

    public GcsFilename getGcsFileName() {
        return mGcsFileName;
    }

    public String getBucketName() {
        return mGcsFileName.getBucketName();
    }

    public String getFileName() {
        return mGcsFileName.getObjectName();
    }

    public long getFileSize() throws IOException {
        return GcsServiceFactory.createGcsService().getMetadata(mGcsFileName).getLength();
    }

    public String getFilePath() {
        return GCS_PATH_ROOT + getBucketName() + "/" + getFileName();
    }

    public String getServingUrl() {
        return GCS_URL_ROOT + getBucketName() + "/" + getFileName();
    }
}
