package com.fizzbuzz.vroom.sample.webservice.biz;

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

import com.fizzbuzz.vroom.core.biz.EntityBiz;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.sample.webservice.domain.Image;
import com.fizzbuzz.vroom.sample.webservice.service.datastore.entity.ImageEntity;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

public class ImageBiz
        extends EntityBiz<Image> {

    public ImageBiz(){
        super(new ImageEntity());
    }

    public String getServingUrl(LongKey imageKey, Integer size) {
        //TODO:  Remove test images
        String servingUrl;

        if (imageKey.get() == 0)
            servingUrl = "http://files.parse.com/6e12c517-a88e-4bf1-ba00-61634788be1e/22879c9f-0d65-46c3-8019" +
                    "-317db6fa8372-puch_up_chest_tap_hold.gif";
        else if (imageKey.get() == 1)
            servingUrl = "http://files.parse.com/6e12c517-a88e-4bf1-ba00-61634788be1e/54824e91-b4a5-4d89-9bb4" +
                    "-71972a110d63-side%20forearm%20bridge%20flex%20extend.gif";
        else if (imageKey.get() == 2)
            servingUrl = "http://files.parse.com/6e12c517-a88e-4bf1-ba00-61634788be1e/22879c9f-0d65-46c3-8019" +
                    "-317db6fa8372-puch_up_chest_tap_hold.gif";
        else {
            // method for Resource class to specify (if requested) image size and generate serving URL
            String fileName = ((ImageEntity) getEntity()).getFileName(imageKey.get());
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withGoogleStorageFileName(fileName);
            if (size != null)
                servingUrlOptions.imageSize(size);

            servingUrl = imagesService.getServingUrl(servingUrlOptions);
        }
        return servingUrl;
    }
}