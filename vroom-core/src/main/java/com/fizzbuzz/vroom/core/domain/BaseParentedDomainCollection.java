package com.fizzbuzz.vroom.core.domain;

import com.fizzbuzz.vroom.core.persist.CollectionPersist;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class BaseParentedDomainCollection<DO extends DomainObject> extends BaseDomainCollection<DO> {
    public BaseParentedDomainCollection(CollectionPersist<DO> persist) {
        super(persist);
    }
}
