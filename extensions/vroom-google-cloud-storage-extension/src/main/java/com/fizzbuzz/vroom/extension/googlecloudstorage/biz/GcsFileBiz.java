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

import com.fizzbuzz.vroom.core.biz.FileEntityBiz;
import com.fizzbuzz.vroom.core.biz.IFileBiz;
import com.fizzbuzz.vroom.core.domain.IUrlAddressableKeyedObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.extension.googlecloudstorage.domain.IGcsFile;
import com.fizzbuzz.vroom.extension.googlecloudstorage.service.datastore.GcsEntity;

public class GcsFileBiz<F extends IGcsFile, E extends GcsEntity<F, ?>>
        extends FileEntityBiz<F>
        implements IUrlAddressableKeyedObject<LongKey>, IFileBiz<F> {

    public GcsFileBiz(E entity) {
        super(entity);
    }
    @Override
    public String getServingUrl(LongKey fileKey) {
        String result = ((E) getEntity()).getGcsFileObject(fileKey.get()).getServingUrl();
        return result;
    }
}