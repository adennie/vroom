package com.fizzbuzz.vroom.core.persist;

import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.domain.ParentedDomainObject;
import com.googlecode.objectify.Key;

import java.util.List;

import static com.fizzbuzz.vroom.core.persist.PersistManager.getOfyService;


/**
 * Copyright (c) 2013 Fizz Buzz LLC
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
        PDO extends DomainObject,
        CDO extends ParentedDomainObject,
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
        List<CDAO> daos = getOfyService().ofy().load().type(getDaoClass())
                .filter("mParentDao", Key.create(mParentDaoClass, mParentId)).list();
        return toDomainCollection(daos);
    }
}
