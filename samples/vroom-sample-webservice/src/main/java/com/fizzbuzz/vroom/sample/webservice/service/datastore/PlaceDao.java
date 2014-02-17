package com.fizzbuzz.vroom.sample.webservice.service.datastore;
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
import com.fizzbuzz.vroom.core.service.datastore.VroomDao;
import com.fizzbuzz.vroom.sample.webservice.domain.Place;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class PlaceDao extends VroomDao<Place> {
    @Index private String name;
    private EmbeddedLocationDao location;

    public EmbeddedLocationDao getLocation() {
        return location;
    }

    @Override
    public Place toDomainObject() {
        return new Place(new LongKey(getId()), name, location.toLocation());
    }

    @Override
    public void fromDomainObject(final Place place) {
        name = place.getName();
        location = new EmbeddedLocationDao(place.getLocation());
    }
}
