package com.fizzbuzz.vroom.extension.googlecloudstorage.service.datastore;
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

import com.fizzbuzz.vroom.core.service.datastore.FileDao;
import com.fizzbuzz.vroom.extension.googlecloudstorage.domain.IGcsFile;


public abstract class GcsDao<F extends IGcsFile> extends FileDao<F> {
    private String bucketName;
    private String gcsFileName;

    @Override
    public void fromDomainObject(final F file) {
        bucketName = file.getBucketName();
        gcsFileName = file.getGcsFileName();
        super.fromDomainObject(file);
    }

    public String getGcsFileName() {
        return gcsFileName;
    }

    public void setGcsFileName(final String gcsFileName) {
        this.gcsFileName = gcsFileName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

}
