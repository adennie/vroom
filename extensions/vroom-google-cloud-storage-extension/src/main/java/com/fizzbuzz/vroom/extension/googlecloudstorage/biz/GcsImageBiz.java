package com.fizzbuzz.vroom.extension.googlecloudstorage.biz;

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
import com.fizzbuzz.vroom.extension.googlecloudstorage.domain.IGcsFile;
import com.fizzbuzz.vroom.extension.googlecloudstorage.service.datastore.GcsEntity;
import com.fizzbuzz.vroom.extension.googlecloudstorage.service.gcs.GcsFileObject;
import com.fizzbuzz.vroom.extension.googlecloudstorage.service.gcs.GcsImageObject;

public class GcsImageBiz<F extends IGcsFile, E extends GcsEntity<F, ?>>
        extends GcsFileBiz<F, E> {

    public GcsImageBiz(E entity) {
        super(entity);
    }

    public String getServingUrl(LongKey fileKey, Integer size) {
        GcsFileObject gcsFileObject = ((E)getEntity()).getGcsFileObject(fileKey);
        GcsImageObject gcsImage = new GcsImageObject(gcsFileObject.getBucketName(), gcsFileObject.getFileName());
        return gcsImage.getServingUrl(size);
    }
}