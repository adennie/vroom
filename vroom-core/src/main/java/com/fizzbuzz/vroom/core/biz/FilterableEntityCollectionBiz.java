package com.fizzbuzz.vroom.core.biz;

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

import com.fizzbuzz.vroom.core.domain.IEntityObject;
import com.fizzbuzz.vroom.core.service.datastore.IFilterableEntityCollection;

import java.util.List;
import java.util.Map;

/**
 * A business logic class for collections of persistent entities, with filtering support.  The filtering
 * constraints are expressed via a generic type.  For example, this could be an enum like
 * <pre>
 * enum MyConstraint {
 *     NAME_EQUALS,
 *     CREATED_AFTER,
 *     OWNED_BY}
 * </pre>
 * The {@link #getFilteredElements} method takes a map of these constraints and values.
 *
 * @param <EO> a domain object type implementing the IEntityObject interface
 * @param <EC> a domain collection type implementing the IFilterableEntityCollection interface
 * @param <FC> a filter constraint type
 */
public class FilterableEntityCollectionBiz<
        EO extends IEntityObject,
        EC extends IFilterableEntityCollection<EO, FC>,
        FC extends Object>
        extends EntityCollectionBiz<EO, EC> {

    public FilterableEntityCollectionBiz(final EC entityCollection) {
        super(entityCollection);
    }

    public List<EO> getFilteredElements(final Map<FC, Object> constraints) {
        return ((IFilterableEntityCollection)getEntityCollection()).getFilteredElements(constraints);
    }

}
