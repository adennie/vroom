package com.fizzbuzz.vroom.core.service.datastore;

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

import com.fizzbuzz.vroom.core.domain.KeyedObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import static com.fizzbuzz.vroom.core.service.datastore.OfyManager.ofy;

public abstract class FilterableEntityCollection<
        KO extends KeyedObject<LongKey>,
        DAO extends Dao<KO>,
        FC>
        extends EntityCollection<KO, DAO>
        implements IFilterableEntityCollection<KO, FC> {

    protected FilterableEntityCollection(final Class<KO> domainElementClass,
                                         final Class<DAO> elementDaoClass,
                                         final Entity<KO, DAO> elementEntity) {
        super(domainElementClass, elementDaoClass, elementEntity);
    }


    /**
     * Returns an Objectify Query object with a filter added to it.  If the query parameter is non-null,
     * the new filter will be added to any existing filters already assigned to it.  If the query parameter is null,
     * a new Query object will be created.
     *
     * @param query an existing Query object to reuse, or null if a new one should be created
     * @param field the field for which the filter should be created
     * @param value the filtering value
     * @return a Query with a newly assigned filter
     */
    protected <DAO, T> Query<DAO> addFilter(final Query<DAO> query, final String field, final T value) {
        Query<DAO> result;
        if (query == null) {
            LoadType<DAO> loader = (LoadType<DAO>) ofy().load().type(getElementDaoClass());
            result = loader.filter(field, value);
        } else
            result = query.filter(field, value);

        return result;
    }
}
