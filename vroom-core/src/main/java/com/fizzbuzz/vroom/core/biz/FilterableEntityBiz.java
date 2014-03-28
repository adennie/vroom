package com.fizzbuzz.vroom.core.biz;

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

import com.fizzbuzz.vroom.core.domain.IEntityObject;
import com.fizzbuzz.vroom.core.service.datastore.IEntity;
import com.fizzbuzz.vroom.core.service.datastore.IFilterableEntity;

import java.util.List;
import java.util.Map;

/**
 * A base class implementing filtering business logic for domain objects stored as datastore entities.  The filtering
 * constraints are expressed via a generic type.  For example, this could be an enum like
 * <pre>
 * enum MyConstraint {
 *     NAME_EQUALS,
 *     CREATED_AFTER,
 *     OWNED_BY}
 * </pre>
 * The {@link #getMatching} method takes a map of these constraints and values.
 *
 * @param <EO> a domain object type implementing the IEntityObject interface
 * @param <E> a domain collection type implementing the IFilterableEntity interface
 * @param <FC> a filter constraint type
 */
public class FilterableEntityBiz<
        EO extends IEntityObject,
        E extends IEntity<EO> &IFilterableEntity<EO, FC>,
        FC extends Object>
        extends EntityBiz<EO>
        implements IFilterableBiz<EO, FC> {

    public FilterableEntityBiz(final E entity) {
        super(entity);
    }

    @Override
    public List<EO> getMatching(final Map<FC, Object> constraints) {
        return ((IFilterableEntity) getEntity()).getMatching(constraints);
    }

}
