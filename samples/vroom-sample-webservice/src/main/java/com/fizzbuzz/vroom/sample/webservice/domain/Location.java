package com.fizzbuzz.vroom.sample.webservice.domain;

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

import com.fizzbuzz.vroom.extension.googlemaps.GeocodingHelper;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderResult;

public class Location {
    private String mRawAddress;
    private String mFormattedAddress;
    private String mLocality;
    private String mAdminAreaLevel1;
    private String mPostalCode;
    private String mCountry;
    private float mLatitude;
    private float mLongitude;

    public Location(final String address, final String formattedAddress, final String locality,
                    final String adminAreaLevel1, final String postalCode, final String country,
                    final float latitude, final float longitude) {
        mRawAddress = address;
        mFormattedAddress = formattedAddress;
        mLocality = locality;
        mAdminAreaLevel1 = adminAreaLevel1;
        mPostalCode = postalCode;
        mCountry = country;
        mLatitude = latitude;
        mLongitude = longitude;
    }


    /**
     * Creates a Location from a raw address (e.g. text entered by a user) and an optional latitude/longitude pair.
     *
     * @param address - the address of the location
     * @param latitude   - the latitude of the location (may be null)
     * @param longitude  - the longitude of the location (may be null)
     */
    public Location(final String address, final Float latitude, final Float longitude) {
        mRawAddress = address;

        GeocoderResult geocoderResult = GeocodingHelper.getBestGeocodingMatch(address);

        mFormattedAddress = geocoderResult.getFormattedAddress();

        for (GeocoderAddressComponent component : geocoderResult.getAddressComponents()) {
            if (component.getTypes().contains("locality"))
                mLocality = component.getLongName();
            if (component.getTypes().contains("administrative_area_level_1"))
                mAdminAreaLevel1 = component.getShortName();
            if (component.getTypes().contains("country"))
                mCountry = component.getShortName();
            if (component.getTypes().contains("postal_code"))
                mPostalCode = component.getShortName();
        }

        if (latitude != null && longitude != null) {
            mLatitude = latitude;
            mLongitude = longitude;
        }
        else {
            mLatitude = geocoderResult.getGeometry().getLocation().getLat().floatValue();
            mLongitude = geocoderResult.getGeometry().getLocation().getLng().floatValue();
        }
    }

    public String getAdminAreaLevel1() {
        return mAdminAreaLevel1;
    }

    public void setAdminAreaLevel1(final String adminAreaLevel1) {
        mAdminAreaLevel1 = adminAreaLevel1;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(final String country) {
        mCountry = country;
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public void setFormattedAddress(final String formattedAddress) {
        mFormattedAddress = formattedAddress;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(final float latitude) {
        mLatitude = latitude;
    }

    public String getLocality() {

        return mLocality;
    }

    public void setLocality(final String locality) {
        mLocality = locality;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(final float longitude) {
        mLongitude = longitude;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(final String postalCode) {
        mPostalCode = postalCode;
    }

    public String getAddress() {
        return mRawAddress;
    }

    public void setAddress(final String address) {
        mRawAddress = address;
    }
}
