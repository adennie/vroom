package com.fizzbuzz.vroom.core.dto_converter;

import com.fizzbuzz.vroom.core.domain.IdObject;
import com.fizzbuzz.vroom.core.resource.IdObjectResource;
import com.fizzbuzz.vroom.dto.Dto;

/**
 * Copyright (c) 2013 Fitivity Inc.
 */

public abstract class IdObjectConverter<DTO extends Dto, IO extends IdObject> implements DomainObjectConverter<DTO,
        IO> {

    final Class<? extends IdObjectResource> mResourceClass;

    public IdObjectConverter(final Class<? extends IdObjectResource> resourceClass) {
        mResourceClass = resourceClass;
    }

    protected String getCanonicalUri(IO idObject) {
        return IdObjectResource.getCanonicalUri(mResourceClass, idObject.getId());
    }

    protected long getIdFromDto(DTO dto) {
        if (dto.getSelfRef() == null)
            return -1;
        else
            return IdObjectResource.getIdFromUri(mResourceClass, dto.getSelfRef());
    }
}
