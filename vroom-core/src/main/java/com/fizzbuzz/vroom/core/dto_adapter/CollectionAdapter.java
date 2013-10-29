package com.fizzbuzz.vroom.core.dto_adapter;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

import com.fizzbuzz.vroom.core.domain.DomainCollection;
import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.dto.CollectionDto;
import com.fizzbuzz.vroom.dto.ObjectDto;

public abstract class CollectionAdapter<
        DTC extends CollectionDto<DTO>,
        DTO extends ObjectDto,
        DC extends DomainCollection<DO>,
        DO extends DomainObject> extends BaseAdapter {

    public CollectionAdapter(final String uriTemplate) {
        super(uriTemplate);
    }

    public abstract DTC toDto(DC domainCollection);
}

