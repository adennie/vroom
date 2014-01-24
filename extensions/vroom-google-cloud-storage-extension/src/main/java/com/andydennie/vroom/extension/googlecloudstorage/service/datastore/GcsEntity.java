package com.andydennie.vroom.extension.googlecloudstorage.service.datastore;
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

import com.andydennie.vroom.core.service.datastore.FileEntity;
import com.andydennie.vroom.extension.googlecloudstorage.domain.IGcsFile;
import com.andydennie.vroom.extension.googlecloudstorage.exception.GcsException;
import com.andydennie.vroom.extension.googlecloudstorage.service.gcs.GcsFileObject;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GcsEntity<F extends IGcsFile, DAO extends GcsDao<F>> extends FileEntity<F, DAO> {
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public GcsEntity(Class<F> doClass, Class<DAO> daoClass) {
        super(doClass, daoClass);
    }

    public void create(final F file, GcsFileOptions gcsFileOptions, final byte[] content) {
        // first, write the file to GCS
        GcsFileObject gcsFileObject = new GcsFileObject(file.getBucketName(), file.getFileName(), gcsFileOptions);
        try {
            gcsFileObject.write(content);
        } catch (IOException e) {
            mLogger.error("IOExcepton caught while writing file content to GCS", e);
            throw new GcsException(e);
        }

        // now save an entity to the datastore, referencing the GCS file
        super.create(file);
    }

    @Override
    public void delete(final Long key) {
        GcsFileObject gcsFileObject = getGcsFile(key);
        try {
            gcsFileObject.delete();
            super.delete(key);
        } catch (IOException e) {
            throw new GcsException("failed to delete GCS file" + gcsFileObject.getFilePath(), e);
        }
    }


    public GcsFileObject getGcsFile(Long key) {
        F file = get(key);
        GcsFileObject result = new GcsFileObject(file.getBucketName(), file.getFileName());
        return result;
    }

    @Override
    protected DAO createDao(final F file) {
        DAO result = super.createDao(file);
        result.setBucketName(file.getBucketName());
        return result;
    }
}
