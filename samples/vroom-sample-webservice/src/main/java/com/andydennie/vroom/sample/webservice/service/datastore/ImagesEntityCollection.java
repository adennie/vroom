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

import com.andydennie.vroom.core.service.datastore.EntityCollection;
import com.andydennie.vroom.sample.webservice.domain.Image;

public class ImagesEntityCollection extends EntityCollection<Image, ImageDao> {

    public ImagesEntityCollection() {
        super(Image.class, ImageDao.class, new ImageEntity());
    }

    public Image addImage(final Image image, final String fileName) {
        return new ImageEntity().create(image, fileName);
    }
}
