package com.andydennie.vroom.core.api.resource;

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

import com.andydennie.vroom.core.biz.ICollectionBiz;
import com.andydennie.vroom.core.domain.DomainCollection;
import com.andydennie.vroom.core.domain.IDomainObject;
import com.andydennie.vroom.core.util.Reflections;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DomainCollectionMigrationResource<
        DC extends DomainCollection<DO>,
        DO extends IDomainObject,
        CB extends ICollectionBiz<DO>>
        extends VroomResource<DC> {

    private Class<DC> mDomainCollectionClass;
    private CB mCollectionBiz;
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    public CB getCollectionBiz() {
        return mCollectionBiz;
    }

    public DC postResource() {
        DC result = Reflections.newInstance(mDomainCollectionClass);
        try {
            //Rewite All Entities
            mCollectionBiz.rewriteAll();

            // return all entities
            result.addAll(mCollectionBiz.getElements());

            getResponse().setStatus(Status.SUCCESS_TRANSFORMATION_APPLIED);
            mLogger.info("Entity Migration complete");
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    public void deleteResource() {
            mCollectionBiz.deleteAll();
    }

    protected void doInit(final Class<DC> domainCollectionClass,
                          final CB collectionBiz) throws ResourceException {
        mDomainCollectionClass = domainCollectionClass;
        mCollectionBiz = collectionBiz;
    }

}
