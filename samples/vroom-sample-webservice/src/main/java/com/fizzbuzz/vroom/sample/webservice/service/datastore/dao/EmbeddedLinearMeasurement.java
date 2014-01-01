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

import com.fizzbuzz.vroom.sample.webservice.domain.LinearMeasurement;
import com.googlecode.objectify.annotation.Embed;

@Embed
public class EmbeddedLinearMeasurement {
    private long quantity;
    private LinearMeasurement.Unit unit;

    // no-arg constructor needed by Objectify
    public EmbeddedLinearMeasurement() {
    }

    public EmbeddedLinearMeasurement(final LinearMeasurement measurement) {
        quantity = measurement.getQuantity();
        unit = measurement.getUnit();
    }

    public LinearMeasurement toLinearMeasurement() {
        return new LinearMeasurement(quantity, unit);
    }
}
