package com.andydennie.vroom.sample.webservice.api.application;

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

import com.andydennie.vroom.core.api.util.UriHelper;

public class Uris {
    /* Many APIs define their URL space using a base URL component such as "/api" or "/v1", or something similar.
       This is optional in Vroom but it is supported via VroomApplication.setUrlRoot.
       Note: this root must also be specified in app.yaml for the "restlet-servlet" and "objectify-filter"
       handlers.
      */
    public static final String API_ROOT = "/api";

    /* the rest of these strings are URL paths that appear after the URL root */
    public static final String IMAGES = "/images/";
    public static final String IMAGE_TEMPLATE = IMAGES + UriHelper.tokenize(UriTokens.IMAGE_ID);
    public static final String IMAGE_UPLOADER = "/image_uploader";
    public static final String PLACES = "/places/";
    public static final String PLACE_TEMPLATE = PLACES + UriHelper.tokenize(UriTokens.PLACE_ID);
    public static final String USERS = "/users/";
    public static final String USER_TEMPLATE = USERS + UriHelper.tokenize(UriTokens.USER_ID);
}
