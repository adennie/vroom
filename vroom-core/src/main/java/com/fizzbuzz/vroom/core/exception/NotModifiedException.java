package com.fizzbuzz.vroom.core.exception;

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

public class NotModifiedException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotModifiedException(final String message) {
        super(message);
    }

    public NotModifiedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotModifiedException(final Throwable cause) {
        super(cause);
    }
}
