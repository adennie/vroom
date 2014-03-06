package com.fizzbuzz.vroom.core.service.datastore;

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

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import static com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService.ofy;

public class OfyUtils {
    public static <DAO extends VroomDao> List<Long> getIdsFromRefs(List<Ref<DAO>> refs) {
        List<Long> ids = new ArrayList<>();
        for (Ref<DAO> ref : refs) {
            ids.add(ref.getKey().getId());
        }
        return ids;
    }

    /**
     * Returns an Objectify Query object with a filter added to it.  If the query parameter is non-null,
     * the new filter will be added to any existing filters already assigned to it.  If the query parameter is null,
     * a new Query object will be created.
     *
     * @param query an existing Query object to reuse, or null if a new one should be created
     * @param field the field for which the filter should be created
     * @param value the filtering value
     * @param daoClass the DAO class
     * @return a Query with a newly assigned filter
     */
    public static <DAO, T> Query<DAO> addFilter(final Query<DAO> query,
                                                final String field,
                                                final T value,
                                                Class<DAO> daoClass) {
        Query<DAO> result;
        if (query == null) {
            LoadType<DAO> loader = (LoadType<DAO>) ofy().load().type(daoClass);
            result = loader.filter(field, value);
        } else
            result = query.filter(field, value);

        return result;
    }
}
