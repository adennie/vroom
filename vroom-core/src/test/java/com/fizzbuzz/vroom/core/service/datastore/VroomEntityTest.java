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

import com.fizzbuzz.vroom.core.domain.EntityObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService.ofy;
import static org.assertj.core.api.Assertions.assertThat;

public class VroomEntityTest {
    @Rule
    public GaeRule dsRule = new GaeRule();
    @Rule
    public ObjectifyRule ofyRule = new ObjectifyRule(new TestOfyService());
    TestEntityObject mTestEntityObject;
    VroomEntity<TestEntityObject, TestDao> mTestEntity;

    @Before
    public void setup() {
        mTestEntityObject = new TestEntityObject();
        mTestEntity = new VroomEntity<TestEntityObject, TestDao>(TestEntityObject.class, TestDao.class) {
        };

    }

    @Test
    public void createAssignsKeyIfKeyedObjectDidntAlreadyHaveOne() {
        assertThat(mTestEntityObject.getKey().get()).isNull();
        mTestEntity.create(mTestEntityObject);
        assertThat(mTestEntityObject.getKey()).isNotNull();
    }

    @Test
    public void createUsesSpecifiedKeyIfProvided() {
        LongKey key = new LongKey(123L);
        mTestEntityObject.setKey(key);
        mTestEntity.create(mTestEntityObject);
        assertThat(mTestEntityObject.getKey())
            .isNotNull()
            .isEqualTo(key);
    }


    @Test
    public void allocateIdReturnsValidId() {
        Long id = mTestEntity.allocateId();
        assertThat(id).isNotNull();
    }

    @Test
    public void dateFieldsAreStampedAtCreateTime() {
        mTestEntity.create(mTestEntityObject);
        TestDao testDao = ofy().load().type(TestDao.class).id(mTestEntityObject.getKey().get()).now();
        assertThat(testDao.createDate).isNotNull().isCloseTo(new Date(System.currentTimeMillis()), 1000);
        assertThat(testDao.modDate).isNotNull().isCloseTo(new Date(System.currentTimeMillis()), 1000);
    }

    @Test
    public void correctDateFieldsAreStampedAtModTime() {
        // create an entity and remember its create and modify dates
        mTestEntity.create(mTestEntityObject);
        TestDao testDao = mTestEntity.loadDao(mTestEntityObject.getKey().get());
        Date origCreateDate = testDao.createDate;
        Date origModDate = testDao.modDate;

        // now update it and reload the DAO
        mTestEntity.update(mTestEntityObject);
        testDao = ofy().load().type(TestDao.class).id(mTestEntityObject.getKey().get()).now();

        // the create date shouldn't have changed
        assertThat(testDao.createDate)
            .isNotNull()
            .isEqualTo(origCreateDate);

        // the mod date should have been updated
        assertThat(testDao.modDate)
            .isNotNull().
            isNotEqualTo(origModDate)
            .isCloseTo(new Date(System.currentTimeMillis()), 1000);
    }


    private static class TestEntityObject extends EntityObject {
        public TestEntityObject() {
            super(new LongKey((Long) null));
        }
    }

    @Entity
    private static class TestDao extends VroomDao<TestEntityObject> {
        public @CreateDate Date createDate;
        public @ModDate Date modDate;

        @Override
        public TestEntityObject toDomainObject() {
            return null;
        }

        @Override
        public void fromDomainObject(final TestEntityObject entityObject) {
        }
    }

    public static class TestOfyService extends OfyService {
        static {
            ObjectifyService.factory().register(TestDao.class);
        }
    }
}
