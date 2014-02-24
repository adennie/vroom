package com.fizzbuzz.vroom.sample.webservice.biz;
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

import com.fizzbuzz.vroom.core.biz.FilterableEntityBiz;
import com.fizzbuzz.vroom.sample.webservice.domain.Place;
import com.fizzbuzz.vroom.sample.webservice.service.datastore.PlaceEntity;

public class PlaceBiz
        extends FilterableEntityBiz<Place, PlaceEntity, PlaceBiz.PlaceConstraint> {


    public enum PlaceConstraint {
        NAME_EQUALS,
        LOCALITY_EQUALS,
        ADMIN_AREA_LEVEL_1_EQUALS,
        POSTAL_CODE_EQUALS
    }
    public PlaceBiz() {
        super(new PlaceEntity());
    }
}
