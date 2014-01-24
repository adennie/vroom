package com.andydennie.vroom.sample.webservice.api.application;

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

import com.andydennie.vroom.core.api.application.VroomApplication;
import com.andydennie.vroom.core.api.resource.ResourceRegistry;
import com.andydennie.vroom.core.api.service.CorsService;
import com.andydennie.vroom.core.service.datastore.OfyService;
import com.andydennie.vroom.sample.webservice.api.dto_converter.PlaceConverter;
import com.andydennie.vroom.sample.webservice.api.dto_converter.PlacesConverter;
import com.andydennie.vroom.sample.webservice.api.dto_converter.UserConverter;
import com.andydennie.vroom.sample.webservice.api.dto_converter.UsersConverter;
import com.andydennie.vroom.sample.webservice.api.resource.EdgeCacheResource;
import com.andydennie.vroom.sample.webservice.api.resource.ImageResource;
import com.andydennie.vroom.sample.webservice.api.resource.ImagesResource;
import com.andydennie.vroom.sample.webservice.api.resource.PlaceResource;
import com.andydennie.vroom.sample.webservice.api.resource.PlacesResource;
import com.andydennie.vroom.sample.webservice.api.resource.UserResource;
import com.andydennie.vroom.sample.webservice.api.resource.UsersResource;
import com.andydennie.vroom.sample.webservice.service.datastore.SampleOfyService;
import com.andydennie.vroom.sample.webservice.util.Environment;
import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.service.MetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class SampleApplication
        extends VroomApplication {

    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);


    public SampleApplication() {
        try {
            mLogger.info("SampleApplication: starting up application: {}", this);
            setName("Vroom sample API");
            setDescription("A sample REST API built with Vroom");
            setOwner("Andy Dennie");
            // Establish configuration for handling CORS requests.
            configureCors();

            // "multipart/form-data" is not one of the media types that are built-in by default in Restlet, so
            // register it, so we can use it for file uploads
            getMetadataService().addExtension(MediaType.MULTIPART_FORM_DATA.getDescription(),
                    MediaType.MULTIPART_FORM_DATA, true);
        } catch (RuntimeException e) {
            mLogger.warn("SampleApplication.ctor: exception caught:", e);
            throw e;
        }
    }

    @Override
    public synchronized void start() throws Exception {

        // register the URL root
        registerUrlRoot(Environment.RESOURCE_ROOT);

        // register the custom media types
        MetadataService metadataService = getMetadataService();
        MediaTypes.register(metadataService);

        // register resource classes with their URI paths
        ResourceRegistry.registerResource(ImageResource.class, Uris.IMAGE_TEMPLATE);
        ResourceRegistry.registerResource(ImagesResource.class, Uris.IMAGES);
        ResourceRegistry.registerResource(PlaceResource.class, Uris.PLACE_TEMPLATE);
        ResourceRegistry.registerResource(PlacesResource.class, Uris.PLACES);
        ResourceRegistry.registerResource(UserResource.class, Uris.USER_TEMPLATE);
        ResourceRegistry.registerResource(UsersResource.class, Uris.USERS);

        ResourceRegistry.registerResource(EdgeCacheResource.class, "/edge_cached");


        // register ID tokens for Keyed Resources
        ResourceRegistry.registerIdToken(ImageResource.class, UriTokens.IMAGE_ID);
        ResourceRegistry.registerIdToken(PlaceResource.class, UriTokens.PLACE_ID);
        ResourceRegistry.registerIdToken(UserResource.class, UriTokens.USER_ID);

        // register DTO converters
        List<ConverterHelper> converterHelpers = Engine.getInstance().getRegisteredConverters();
        converterHelpers.add(new PlaceConverter());
        converterHelpers.add(new PlacesConverter());
        converterHelpers.add(new UserConverter());
        converterHelpers.add(new UsersConverter());

        super.start();
    }

    private void configureCors() {

        CorsService corsService = new CorsService();

        // Set allowed origins based on execution environment. Note: Environment.java is a resource-filtered
        // source file containing values defined at build time via maven properties.
        corsService.getAllowedOrigins().addAll(Arrays.asList(Environment.CORS_ALLOWED_ORIGINS.split(";")));

        corsService.getAllowedHeaders().add("Accept");
        corsService.getAllowedHeaders().add("Content-type");

        // expose Location header in responses to CORS requests
        corsService.getExposedHeaders().add("Location");

        getServices().add(corsService);
    }
    @Override
    protected OfyService getOfyService() {
        return new SampleOfyService();
    }
}
