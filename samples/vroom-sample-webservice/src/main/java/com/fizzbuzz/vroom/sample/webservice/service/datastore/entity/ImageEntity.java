package com.fizzbuzz.vroom.sample.webservice.service.datastore.entity;
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

import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.service.datastore.TimeStampedEntity;
import com.fizzbuzz.vroom.sample.webservice.domain.Image;
import com.fizzbuzz.vroom.sample.webservice.service.datastore.dao.ImageDao;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageEntity extends TimeStampedEntity<Image, ImageDao> {

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public ImageEntity() {
        super(Image.class, ImageDao.class);
    }

    @Override
    public Image get(final Long key) {
        return super.get(key);
    }

    public Image create(final Image image, final String fileName) {
        ImageDao dao = new ImageDao(image, fileName);
        saveDao(dao);
        image.setKey(new LongKey(dao.getId()));
        mLogger.debug("Image key is: " + image.getKeyAsString());
        return image;
    }

    @Override
    public void delete(final Long key) {
        ImageDao imageDao = getDao(key);
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        BlobKey blobKey = blobstoreService.createGsBlobKey(imageDao.getFileName());
        blobstoreService.delete(blobKey);
        super.delete(key);
    }

    public String getFileName(final Long imageKey) {
        ImageDao imageDao = getDao(imageKey);
        return imageDao.getFileName();
    }
}
