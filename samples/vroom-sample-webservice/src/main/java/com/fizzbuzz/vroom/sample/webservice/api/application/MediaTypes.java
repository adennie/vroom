package com.fizzbuzz.vroom.sample.webservice.api.application;

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

import org.restlet.data.MediaType;
import org.restlet.service.MetadataService;

public class MediaTypes {

    public static void register(final MetadataService metadataService) {
        PlaceMediaTypes.register(metadataService);
        PlacesMediaTypes.register(metadataService);
        UserMediaTypes.register(metadataService);
        UsersMediaTypes.register(metadataService);
    }

    public static class PlaceMediaTypes {
        // v1.0
        public static final String JSON_V1_0 = "place-1.0-json";
        public static final MediaType JSON_V1_0_MEDIATYPE =
                MediaType.register("application/vnd.vroomsample.place-v1+json; level=0",
                        "Vroom Place JSON version 1.0");

        public static void register(final MetadataService metadataService) {
            metadataService.addExtension(JSON_V1_0, JSON_V1_0_MEDIATYPE);
        }
    }

    public static class PlacesMediaTypes {
        // v1.0
        public static final String JSON_V1_0 = "places-1.0-json";
        public static final MediaType JSON_V1_0_MEDIATYPE =
                MediaType.register("application/vnd.vroomsample.places-v1+json; level=0",
                        "Vroom Places JSON version 1.0");

        public static void register(final MetadataService metadataService) {
            metadataService.addExtension(JSON_V1_0, JSON_V1_0_MEDIATYPE);
        }
    }
    public static class UserMediaTypes {
        // v1.0
        public static final String JSON_V1_0 = "user-1.0-json";
        public static final MediaType JSON_V1_0_MEDIATYPE =
                MediaType.register("application/vnd.vroomsample.user-v1+json; level=0",
                        "Vroom User JSON version 1.0");

        public static void register(final MetadataService metadataService) {
            metadataService.addExtension(JSON_V1_0, JSON_V1_0_MEDIATYPE);
        }
    }

    public static class UsersMediaTypes {
        // v1.0
        public static final String JSON_V1_0 = "users-1.0-json";
        public static final MediaType JSON_V1_0_MEDIATYPE =
                MediaType.register("application/vnd.vroomsample.users-v1+json; level=0",
                        "Vroom Users JSON version 1.0");

        public static void register(final MetadataService metadataService) {
            metadataService.addExtension(JSON_V1_0, JSON_V1_0_MEDIATYPE);
        }
    }

}
