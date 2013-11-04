package com.fizzbuzz.vroom.core.persist;

import com.fizzbuzz.vroom.core.domain.IdObject;
import com.fizzbuzz.vroom.core.domain.ParentedIdObject;
import com.googlecode.objectify.Key;

import java.util.List;

import static com.fizzbuzz.vroom.core.persist.PersistManager.getOfyService;


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
 * A base persist class for managing read-only collections of entities which are logically parented by another entity.
 * The child entity stores a reference to the key of its parent, allowing the {@link #getDomainElements()} method to
 * constrain the results to those belonging to that particular parent.
 * <p/>
 * Note: the term "parent" in this context is not related to Objectify's @Parent annotation, which is used to
 * designate an ancestor within an entity group.  The entities managed by this class may or may not be part of an
 * entity group.
 *
 * @param <PDO>  the parent domain object type
 * @param <CDO>  the child domain object type
 * @param <PDAO> the parent DAO type
 * @param <CDAO> the child DAO type
 */
public abstract class BaseParentedCollectionPersist<
        PDO extends IdObject,
        CDO extends ParentedIdObject,
        PDAO extends BaseDao<PDO>,
        CDAO extends ParentedDao<PDAO, PDO, CDO>>
        extends BaseCollectionPersist<CDO, CDAO> {

    private Class<PDAO> mParentDaoClass;
    private long mParentId;

    protected BaseParentedCollectionPersist(final Class<PDAO> parentDaoClass,
                                  final long parentId,
                                  final Class<CDO> childDomainObjectClass,
                                  final Class<CDAO> childDaoClass) {
        super(childDomainObjectClass, childDaoClass);
        mParentDaoClass = parentDaoClass;
        mParentId = parentId;
    }

    public long getParentId() {
        return mParentId;
    }

    @Override
    public List<CDO> getDomainElements() {
        List<CDAO> daos = getOfyService().ofy().load().type(getElementDaoClass())
                .filter("mParentDao", Key.create(mParentDaoClass, mParentId)).list();
        return toDomainCollection(daos);
    }
}
