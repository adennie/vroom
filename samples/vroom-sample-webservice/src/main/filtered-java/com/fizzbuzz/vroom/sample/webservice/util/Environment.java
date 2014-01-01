package com.fizzbuzz.vroom.sample.webservice.util;

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

/**
 * A helper class containing environment-specific fields and methods.  This class is filtered by
 * maven-resources-plugin, which replaces references to maven variables with their values (typically
 * defined within maven profiles).
 */
public class Environment {
    // a semicolon-delimited list of allowed origins for Cross-Origin Resource Sharing
    public static final String CORS_ALLOWED_ORIGINS = "${vroom.sample.webservice.env.cors.allowed-origins}";
    public static final String GCS_BUCKET_IMAGES = "${vroom.sample.webservice.env.gcs.bucket.images}";
}
