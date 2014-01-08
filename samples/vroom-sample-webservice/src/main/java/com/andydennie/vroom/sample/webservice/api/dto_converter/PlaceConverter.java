package com.andydennie.vroom.sample.webservice.api.dto_converter;

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

import com.andydennie.vroom.core.api.dto_converter.KeyedObjectConverter;
import com.andydennie.vroom.sample.dto.LocationProperty;
import com.andydennie.vroom.sample.dto.PlaceDto;
import com.andydennie.vroom.sample.webservice.api.application.MediaTypes;
import com.andydennie.vroom.sample.webservice.api.resource.PlaceResource;
import com.andydennie.vroom.sample.webservice.domain.Location;
import com.andydennie.vroom.sample.webservice.domain.Place;
import org.restlet.data.MediaType;

public class PlaceConverter
        extends KeyedObjectConverter<PlaceDto, Place> {

    public PlaceConverter() {
        super(PlaceResource.class,
                PlaceDto.class,
                Place.class,
                MediaTypes.PlaceMediaTypes.JSON_V1_0_MEDIATYPE,
                MediaType.APPLICATION_JSON);
    }

    @Override
    public PlaceDto toDto(final Place place) {
        LocationProperty location = new LocationProperty(
                place.getLocation().getAddress(),
                place.getLocation().getLatitude(),
                place.getLocation().getLongitude());
        return new PlaceDto(getCanonicalUri(place), place.getName(), location);
    }

    @Override
    public Place toObject(final PlaceDto dto) {
        Location location = new Location(
                dto.getLocation().getAddress(),
                dto.getLocation().getLatitude(),
                dto.getLocation().getLongitude());
        return new Place(getIdFromDto(dto), dto.getName(), location);
    }
}
