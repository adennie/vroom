package com.andydennie.vroom.sample.webservice.api.resource;

/*
 * Copyright (c) 2014 Andy Dennie
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

import com.andydennie.vroom.core.api.resource.KeyedCollectionResource;
import com.andydennie.vroom.sample.webservice.api.application.MediaTypes;
import com.andydennie.vroom.sample.webservice.biz.PlacesBiz;
import com.andydennie.vroom.sample.webservice.domain.Place;
import com.andydennie.vroom.sample.webservice.domain.Places;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.HashMap;
import java.util.Map;

public class PlacesResource
        extends KeyedCollectionResource<Places, Place, PlacesBiz> {

    static final String PARAM_NAME = "name";
    static final String PARAM_CITY = "city";
    static final String PARAM_LOCALITY = "locality";
    static final String PARAM_STATE = "state";
    static final String PARAM_ADMIN_AREA_LEVEL_1 = "admin_area_level_1";
    static final String PARAM_ZIP_CODE = "zip_code";
    static final String PARAM_POSTAL_CODE = "postal_code";

    @Override
    @Get(MediaTypes.PlacesMediaTypes.JSON_V1_0 + "|json")
    public Places getResource() {
        Places result = new Places();
        try {

            Map<PlacesBiz.PlaceConstraint, Object> constraints = new HashMap<>();

            String name = getStringParamValue(PARAM_NAME);
            if (name != null)
                constraints.put(PlacesBiz.PlaceConstraint.NAME_EQUALS, name);

            String locality = getStringParamValue(PARAM_CITY);
            if (locality != null)
                constraints.put(PlacesBiz.PlaceConstraint.LOCALITY_EQUALS, locality);

            locality = getStringParamValue(PARAM_LOCALITY);
            if (locality != null)
                constraints.put(PlacesBiz.PlaceConstraint.LOCALITY_EQUALS, locality);

            String adminAreaLevelOne = getStringParamValue(PARAM_STATE);
            if (adminAreaLevelOne != null)
                constraints.put(PlacesBiz.PlaceConstraint.ADMIN_AREA_LEVEL_1_EQUALS, adminAreaLevelOne);

            adminAreaLevelOne = getStringParamValue(PARAM_ADMIN_AREA_LEVEL_1);
            if (adminAreaLevelOne != null)
                constraints.put(PlacesBiz.PlaceConstraint.ADMIN_AREA_LEVEL_1_EQUALS, adminAreaLevelOne);

            String postalCode = getStringParamValue(PARAM_ZIP_CODE);
            if (postalCode != null)
                constraints.put(PlacesBiz.PlaceConstraint.POSTAL_CODE_EQUALS, postalCode);

            postalCode = getStringParamValue(PARAM_POSTAL_CODE);
            if (postalCode != null)
                constraints.put(PlacesBiz.PlaceConstraint.POSTAL_CODE_EQUALS, postalCode);

            if (constraints.isEmpty())
                result.addAll(super.getResource());
            else
                result.addAll(((PlacesBiz) getCollectionBiz()).getFilteredElements(constraints));
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Override
    @Post(MediaTypes.PlaceMediaTypes.JSON_V1_0 + "|json" + ":"
            + MediaTypes.PlaceMediaTypes.JSON_V1_0 + "|json")
    public Place postResource(final Place place) {
        Place result = null;
        try {
            result = super.postResource(place);
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Override
    @Delete
    public void deleteResource() {
        try {
            super.deleteResource();
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Override
    protected void doInit() {
        super.doInit(Places.class, new PlacesBiz(), PlaceResource.class);
    }
}
