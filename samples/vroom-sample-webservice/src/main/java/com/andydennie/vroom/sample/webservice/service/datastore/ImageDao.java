package com.andydennie.vroom.sample.webservice.service.datastore;
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

import com.andydennie.vroom.core.service.datastore.TimeStampedDao;
import com.andydennie.vroom.sample.webservice.domain.Image;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class ImageDao extends TimeStampedDao<Image> {
    private String fileName;

    // no-arg constructor needed by Objectify
    public ImageDao() {
    }

    /**
     * This constructor is invoked via reflection by VroomEntity and EntityCollection.
     *
     * @param image an Image domain object
     */
    public ImageDao(final Image image, String fileName) {
        super(image);
        this.fileName = fileName;
    }

    @Override
    public Image toDomainObject() {
        return new Image(getId());
    }

    public String getFileName() {
        return fileName;
    }
}
