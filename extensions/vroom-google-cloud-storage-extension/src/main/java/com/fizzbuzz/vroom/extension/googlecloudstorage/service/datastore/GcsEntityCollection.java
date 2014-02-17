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

import com.fizzbuzz.vroom.core.service.datastore.EntityCollection;
import com.fizzbuzz.vroom.core.service.datastore.IFileEntityCollection;
import com.fizzbuzz.vroom.extension.googlecloudstorage.domain.IGcsFile;

public class GcsEntityCollection<
        F extends IGcsFile,
        DAO extends GcsDao<F>,
        ENTITY extends GcsEntity<F, DAO>>
        extends EntityCollection<F, DAO, ENTITY>
        implements IFileEntityCollection<F> {

    public GcsEntityCollection(ENTITY entity) {
        super(entity);
    }

    @Override
    public void addElement(final F file, final byte[] content) {
        getElementEntity().create(file, content);
    }
}
