package com.fizzbuzz.vroom.core.biz;

/*
 * Copyright (c) 2013 Fizz Buzz LLC
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

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.service.datastore.entity.FilterableEntityCollection;

import java.util.List;
import java.util.Map;

/**
 * A business logic class for collections of persistent entities, with filtering support.  The filtering
 * constraints are expressed via a parameterized type.  For example, this could be an enum like
 * <pre>
 * enum MyConstraint {
 *     NAME_EQUALS,
 *     CREATED_AFTER,
 *     OWNED_BY}
 * </pre>
 * The {@link #getFilteredElements} method takes a map of these constraints and values.
 * }
 * @param <KO> a KeyedObject subtype
 * @param <FC> a filter constraint type
 */
public class FilterableEntityCollectionBiz<KO extends KeyedObject, FC extends Object> extends
        EntityCollectionBiz<KO> {

    public FilterableEntityCollectionBiz(final FilterableEntityCollection<KO, FC> entityCollection) {
        super(entityCollection);
    }

    public List<KO> getFilteredElements(final Map<FC, Object> constraints) {
        return ((FilterableEntityCollection)getEntityCollection()).getFilteredElements(constraints);
    }

}
