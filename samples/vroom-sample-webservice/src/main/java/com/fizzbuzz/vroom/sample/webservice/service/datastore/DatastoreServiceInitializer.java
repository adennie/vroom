package com.fizzbuzz.vroom.sample.webservice.service.datastore;

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

import com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService;
import com.fizzbuzz.vroom.extension.jodatime.JodaDateTimeStamper;
import org.joda.time.DateTime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DatastoreServiceInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // register our SampleOfyService with VroomDatastoreService.
        VroomDatastoreService.registerOfyService(new SampleOfyService());

        // add our JodaDateTimeStamper to VroomDatastoreService's map of time stampers,
        // so that it can stamp DateTime fields in DAOs.
        VroomDatastoreService.addDateTimeStamper(DateTime.class, new JodaDateTimeStamper());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
