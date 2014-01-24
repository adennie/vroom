package com.andydennie.vroom.sample.webservice.biz;
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

import com.andydennie.vroom.sample.webservice.service.datastore.PlaceEntity;
import com.andydennie.vroom.core.biz.EntityBiz;
import com.andydennie.vroom.sample.webservice.domain.Place;

public class PlaceBiz
        extends EntityBiz<Place> {

    public PlaceBiz() {
        super(new PlaceEntity());
    }
}
