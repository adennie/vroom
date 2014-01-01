package com.fizzbuzz.vroom.sample.webservice.service.datastore.dao;
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

import com.fizzbuzz.vroom.core.service.datastore.TimeStampedDao;
import com.fizzbuzz.vroom.sample.webservice.domain.Place;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class PlaceDao extends TimeStampedDao<Place> {
    @Index private String name;
    private EmbeddedLocationDao location;

    // no-arg constructor needed by Objectify
    public PlaceDao() {
    }

    /**
     * This constructor is invoked via reflection by Entity and EntityCollection.
     *
     * @param place a Place domain object
     */
    public PlaceDao(final Place place) {
        super(place.getKey().get());
        name = place.getName();
        location = new EmbeddedLocationDao(place.getLocation());
    }

    public EmbeddedLocationDao getLocation() {
        return location;
    }

    @Override
    public Place toDomainObject() {
        return new Place(getId(), name, location.toLocation());
    }
}
