package com.fizzbuzz.vroom.core.domain;

import com.fizzbuzz.vroom.core.persist.CollectionPersist;

import java.util.List;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class BaseDomainCollection<DO extends DomainObject> implements DomainCollection<DO> {
    private CollectionPersist<DO> mPersist;

    public BaseDomainCollection(CollectionPersist<DO> persist) {
        mPersist = persist;
    }

    @Override
    public List<DO> getElements() {
        return mPersist.getDomainElements();
    }

    CollectionPersist<DO> getPersist() {
        return mPersist;
    }

    @Override
    public DO add(final DO domainObject) {
        domainObject.validate();
        return (DO)getPersist().add(domainObject);
    }

    @Override
    public void deleteAll() {
        getPersist().deleteAll();
    }

    @Override
    public void delete(List<DO> domainObjects) {
        getPersist().delete(domainObjects);
    }
}
