package com.fizzbuzz.vroom.extension.jodatime;

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

import com.fizzbuzz.vroom.core.service.datastore.IDateTimeStamper;
import org.joda.time.DateTime;

import java.lang.reflect.Field;

public class JodaDateTimeStamper implements IDateTimeStamper<DateTime> {
    @Override
    public void stamp(Object target, Field field, DateTime dateTimeValue) {
        if (!field.getType().equals(DateTime.class))
            throw new IllegalArgumentException("field is not of type org.joda.time.DateTime: " + field.getType());

        field.setAccessible(true);
        try {
            field.set(target, dateTimeValue);
        } catch (IllegalAccessException e) {
            // squelch
        }
    }

    @Override
    public void stampWithNow(Object target, Field field) {
        stamp(target, field, DateTime.now());
    }
}
