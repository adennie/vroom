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

import com.fizzbuzz.vroom.core.service.datastore.FilterableEntityCollection;
import com.fizzbuzz.vroom.sample.webservice.biz.PlacesBiz;
import com.fizzbuzz.vroom.sample.webservice.domain.Place;
import com.fizzbuzz.vroom.sample.webservice.service.datastore.dao.PlaceDao;
import com.googlecode.objectify.cmd.Query;

import java.util.List;
import java.util.Map;

public class PlacesEntityCollection extends FilterableEntityCollection<Place, PlaceDao, PlacesBiz.PlaceConstraint> {

    public PlacesEntityCollection() {
        super(Place.class, PlaceDao.class, new PlaceEntity());
    }

    @Override
    public List<Place> getFilteredElements(final Map<PlacesBiz.PlaceConstraint, Object> constraints) {

        if (constraints.isEmpty())
            return getElements();

        Query<PlaceDao> query = null;

        for (Map.Entry<PlacesBiz.PlaceConstraint, Object> constraint : constraints.entrySet()) {
            if (constraint.getKey().equals(PlacesBiz.PlaceConstraint.NAME_EQUALS)) {
                query = addFilter(query, "name", constraint.getValue());
            }

            if (constraint.getKey().equals(PlacesBiz.PlaceConstraint.LOCALITY_EQUALS)) {
                query = addFilter(query, "location.locality", constraint.getValue());
            }

            if (constraint.getKey().equals(PlacesBiz.PlaceConstraint.ADMIN_AREA_LEVEL_1_EQUALS)) {
                query = addFilter(query, "location.adminAreaLevel1", constraint.getValue());
            }

            if (constraint.getKey().equals(PlacesBiz.PlaceConstraint.POSTAL_CODE_EQUALS)) {
                query = addFilter(query, "location.postalCode", constraint.getValue());
            }
        }

        List<PlaceDao> daos = query.list();
        return toDomainCollection(daos);
    }
}