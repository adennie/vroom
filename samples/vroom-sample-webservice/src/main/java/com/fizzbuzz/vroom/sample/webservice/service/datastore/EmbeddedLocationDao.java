package com.fizzbuzz.vroom.sample.webservice.service.datastore;
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

import com.fizzbuzz.vroom.sample.webservice.domain.Location;
import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Index;

@Embed
public class EmbeddedLocationDao {
    private String address;
    private String formattedAddress;
    @Index private String locality;
    @Index private String adminAreaLevel1;
    @Index private String postalCode;
    private String country;
    private GeoPt geoPt;

    // no-arg constructor needed for reflective instantiation by Objectify and Vroom
    public EmbeddedLocationDao() {
    }

    public EmbeddedLocationDao(final Location location) {
        setLocation(location);
    }

    public Location toLocation() {
        return new Location(address, formattedAddress, locality, adminAreaLevel1, postalCode, country,
                geoPt.getLatitude(), geoPt.getLongitude());
    }

    public void setLocation(final Location location) {
        address = location.getAddress();
        formattedAddress = location.getFormattedAddress();
        adminAreaLevel1 = location.getAdminAreaLevel1();
        locality = location.getLocality();
        postalCode = location.getPostalCode();
        country = location.getCountry();
        geoPt = new GeoPt(location.getLatitude(), location.getLongitude());
    }
}
