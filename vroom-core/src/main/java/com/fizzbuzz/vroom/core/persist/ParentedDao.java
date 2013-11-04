package com.fizzbuzz.vroom.core.persist;

import com.fizzbuzz.vroom.core.domain.IdObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Load;

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

/**
 * An abstract base class for DAOs that logically reside below a parent DAO. These DAOs store a references to the
 * parent DAO.
 * <p/>
 * Note: the term "parent" in this context is not related to Objectify's @Parent annotation, which is used to designate
 * an ancestor within an entity group.  A ParentedDao may or may not be part of an entity group.
 *
 * @param <PDAO> the parent DAO type
 * @param <PDO>  the parent domain object type
 * @param <CDO>  the child domain object type
 */
public abstract class ParentedDao<PDAO extends BaseDao<PDO>,
        PDO extends IdObject,
        CDO extends IdObject>
        extends BaseDao<CDO> {
    @Load(OfyLoadGroups.Deep.class)
    Ref<PDAO> mParentDao; // reference to the container entity

    // no-arg constructor used by Objectify
    public ParentedDao() {
    }

    public ParentedDao(final Class<PDAO> parentDaoClass, final long parentId) {
        mParentDao = Ref.create(Key.create(parentDaoClass, parentId));
    }

    protected long getParentId() {
        return mParentDao.getKey().getId();
    }
}
