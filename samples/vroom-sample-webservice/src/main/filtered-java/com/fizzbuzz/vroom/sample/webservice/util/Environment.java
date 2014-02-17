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
 * maven-resources-plugin (see pom.xml), which replaces references to maven properties with their values.  These
 * maven properties are defined within maven profiles in pom.xml, with different values for different profiles, each
 * corresponding to an execution environment (a local development environment, a test environment, and a
 * production environment).
 */
public class Environment {
    // a semicolon-delimited list of allowed origins for Cross-Origin Resource Sharing.  In a local development
    // environment, something like "*" is probably fine, but in a test or production environment you may want to
    // restrict this to specific origins.
    public static final String CORS_ALLOWED_ORIGINS = "${vroom-sample.env.cors.allowed-origins}";

    // Some API implementations define a URL space with a root URL component that precedes all resource paths.   This
    // is optional in Vroom, but it is supported via VroomApplication.setUrlRoot.   Note: this root must also be
    // specified in app.yaml when configuring the "restlet-servlet" and "objectify-filter" handlers.
    public static final String RESOURCE_ROOT = "${vroom-sample.env.resource-root}";

}
