package com.fizzbuzz.vroom.sample.dto;

/*
 * Copyright (c) 2014 Fizz Buzz LLC
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

import com.fizzbuzz.vroom.dto.SimpleDto;

/**
 * A data transfer object representing a place definition
 */
public class UserDto extends SimpleDto {
    private String firstName;
    private String lastName;
    private String email;
    private String profileImageRef;
    private String homeRef;


    // default constructor needed by Jackson to create objects via reflection
    public UserDto() {
    }

    public UserDto(final String selfRef, final String firstName, final String lastName, final String email,
                   final String profileImageRef, final String homeRef) {

        super(selfRef);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImageRef = profileImageRef;
        this.homeRef = homeRef;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getProfileImageRef() {
        return profileImageRef;
    }

    public void setProfileImageRef(final String profileImageRef) {
        this.profileImageRef = profileImageRef;
    }

    public String getHomeRef() {
        return homeRef;
    }

    public void setHomeRef(final String homeRef) {
        this.homeRef = homeRef;
    }
}
