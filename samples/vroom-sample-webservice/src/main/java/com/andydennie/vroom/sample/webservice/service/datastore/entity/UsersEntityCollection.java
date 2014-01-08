package com.andydennie.vroom.sample.webservice.service.datastore.entity;

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

import com.andydennie.vroom.core.domain.DomainCollection;
import com.andydennie.vroom.core.service.datastore.FilterableEntityCollection;
import com.andydennie.vroom.sample.webservice.biz.UsersBiz;
import com.andydennie.vroom.sample.webservice.domain.User;
import com.andydennie.vroom.sample.webservice.service.datastore.dao.UserDao;
import com.googlecode.objectify.cmd.Query;

import java.util.List;
import java.util.Map;

public class UsersEntityCollection extends FilterableEntityCollection<User, UserDao, UsersBiz.UserConstraint> {

    public UsersEntityCollection() {
        super(User.class, UserDao.class, new UserEntity());
    }

    @Override
    public DomainCollection<User> getFilteredElements(final Map<UsersBiz.UserConstraint, Object> constraints) {

        if (constraints.isEmpty())
            return getElements();

        Query<UserDao> query = null;

        for (Map.Entry<UsersBiz.UserConstraint, Object> constraint : constraints.entrySet()) {
            if (constraint.getKey().equals(UsersBiz.UserConstraint.EMAIL_EQUALS)) {
                query = addFilter(query, "email", constraint.getValue());
            }
        }

        List<UserDao> daos = query.list();
        return toDomainCollection(daos);
    }
}