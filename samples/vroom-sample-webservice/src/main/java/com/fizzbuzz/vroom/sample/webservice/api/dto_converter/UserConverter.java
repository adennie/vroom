package com.fizzbuzz.vroom.sample.webservice.api.dto_converter;

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

import com.fizzbuzz.vroom.core.api.dto_converter.KeyedObjectConverter;
import com.fizzbuzz.vroom.core.api.resource.ResourceRegistry;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.sample.dto.UserDto;
import com.fizzbuzz.vroom.sample.webservice.api.application.MediaTypes;
import com.fizzbuzz.vroom.sample.webservice.api.resource.ImageResource;
import com.fizzbuzz.vroom.sample.webservice.api.resource.PlaceResource;
import com.fizzbuzz.vroom.sample.webservice.api.resource.UserResource;
import com.fizzbuzz.vroom.sample.webservice.domain.User;
import org.restlet.data.MediaType;

public class UserConverter
        extends KeyedObjectConverter<UserDto, User> {

    public UserConverter() {
        super(UserResource.class,
                UserDto.class,
                User.class,
                MediaTypes.UserMediaTypes.JSON_V1_0_MEDIATYPE,
                MediaType.APPLICATION_JSON);
    }

    @Override
    public UserDto toDto(final User user) {

        String profileImageRef = null;
        LongKey profileImageKey = user.getProfileImageKey();
        if (user.getProfileImageKey() != null)
            profileImageRef = ResourceRegistry.getCanonicalUri(ImageResource.class, profileImageKey.toString());

        String homeRef = null;
        LongKey homeKey = user.getHomeKey();
        if (homeKey != null)
            ResourceRegistry.getCanonicalUri(PlaceResource.class, homeKey.toString());

        return new UserDto(getCanonicalUri(user), user.getFirstName(), user.getLastName(), user.getEmail(),
                profileImageRef, homeRef);
    }

    @Override
    public User toObject(final UserDto dto) {
        LongKey profileImageKey = null;
        String imageRef = dto.getProfileImageRef();
        if (imageRef != null)
            profileImageKey = new LongKey(ResourceRegistry.getIdFromUri(ImageResource.class, imageRef));

        LongKey homeKey = null;
        String homeRef = dto.getHomeRef();
        if (homeRef != null)
            homeKey = new LongKey(ResourceRegistry.getIdFromUri(PlaceResource.class, homeRef));

        return new User(new LongKey(getIdFromDto(dto)), dto.getFirstName(), dto.getLastName(), dto.getEmail(),
                profileImageKey, homeKey);
    }
}
