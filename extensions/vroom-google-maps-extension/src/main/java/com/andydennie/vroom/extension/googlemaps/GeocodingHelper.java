package com.andydennie.vroom.extension.googlemaps;

/**
 * Copyright (c) 2013 Fitivity Inc.
 */

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

public class GeocodingHelper {
    public static GeocoderResult getBestGeocodingMatch(String address) {
        // call Geocoding API to get structured address fields and store them into the DAO
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest =
                new GeocoderRequestBuilder().setAddress(address).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

        // TODO handle errors

        return geocoderResponse.getResults().get(0);
    }
}