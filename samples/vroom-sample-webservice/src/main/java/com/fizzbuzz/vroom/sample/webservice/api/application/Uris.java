package com.fizzbuzz.vroom.sample.webservice.api.application;

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

import com.fizzbuzz.vroom.core.api.util.UriHelper;

public class Uris {
    public static final String V1_ROOT = "/v1";
    public static final String IMAGES = "/images/";
    public static final String IMAGE_TEMPLATE = IMAGES + UriHelper.tokenize(UriTokens.IMAGE_ID);
    public static final String IMAGE_UPLOADER = "/image_uploader";
    public static final String PLACES = "/places/";
    public static final String PLACE_TEMPLATE = PLACES + UriHelper.tokenize(UriTokens.PLACE_ID);
}
