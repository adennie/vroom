package com.fizzbuzz.vroom.sample.webservice.api;

import com.fizzbuzz.vroom.core.api.VroomApiModule;
import com.fizzbuzz.vroom.sample.webservice.api.resource.*;
import dagger.Module;

@Module(injects = {EdgeCacheResource.class, ImageResource.class, ImagesResource.class, PlaceResource.class,
        PlacesResource.class, UserResource.class, UsersResource.class},
        includes = {VroomApiModule.class})
public class ApiModule {
}
