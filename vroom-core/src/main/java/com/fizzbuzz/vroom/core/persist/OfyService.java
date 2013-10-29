package com.fizzbuzz.vroom.core.persist;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public abstract class OfyService {
    public Objectify ofy() {
        return ObjectifyService.ofy();
    }
}
