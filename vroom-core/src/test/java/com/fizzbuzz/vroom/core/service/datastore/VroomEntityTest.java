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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fizzbuzz.vroom.core.service.datastore.VroomDatastoreService.ofy;
import static org.assertj.core.api.Assertions.assertThat;

public class VroomEntityTest {
    @Rule
    public GaeRule dsRule = new GaeRule.Builder().withDatastore().build();
    @Rule
    public ObjectifyRule ofyRule = new ObjectifyRule(new TestOfyService());
    TestEntityObject mTestEntityObject;
    TestEntity mTestEntity;

    @Before
    public void setup() {
        mTestEntityObject = new TestEntityObject();
        mTestEntity = new TestEntity();

    }

    @Test
    public void create_assignsKeyIfKeyedObjectDidntAlreadyHaveOne() {
        assertThat(mTestEntityObject.getKey().get()).isNull();
        mTestEntity.create(mTestEntityObject);
        assertThat(mTestEntityObject.getKey()).isNotNull();
    }

    @Test
    public void create_usesSpecifiedKeyIfProvided() {
        LongKey key = new LongKey(123L);
        mTestEntityObject.setKey(key);
        mTestEntity.create(mTestEntityObject);
        assertThat(mTestEntityObject.getKey())
                .isNotNull()
                .isEqualTo(key);
    }


    @Test
    public void allocateId_returnsValidId() {
        Long id = mTestEntity.allocateId();
        assertThat(id).isNotNull();
    }

    @Test
    public void create_stampsDateFields() {
        mTestEntity.create(mTestEntityObject);
        TestDao testDao = ofy().load().type(TestDao.class).id(mTestEntityObject.getKey().get()).now();
        assertThat(testDao.createDate).isNotNull().isCloseTo(new Date(System.currentTimeMillis()), 1000);
        assertThat(testDao.modDate).isNotNull().isCloseTo(new Date(System.currentTimeMillis()), 1000);
    }

    @Test
    public void update_stampsDateFields() {
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

    @Test
    public void checkForInboundRefs_detectsInboundRefsFromSingularAnno() {
        VroomEntity<TestEntityObject, ReferencedDao1> referencedEntity =
                new VroomEntity<TestEntityObject, ReferencedDao1>(TestEntityObject.class, ReferencedDao1.class) {
                };
        // create and save a referenced entity
        referencedEntity.create(mTestEntityObject);
        ReferencedDao1 referencedDao = referencedEntity.loadDao(mTestEntityObject.getKey().get());

        // create and save a ReferringDao1 entity that refers to referencedDao's ID
        ReferringObject referringObject = new ReferringObject(mTestEntityObject.getKey());
        VroomEntity<ReferringObject, ReferringDao1> referringEntity =
                new VroomEntity<ReferringObject, ReferringDao1>(ReferringObject.class, ReferringDao1.class) {
                };
        referringEntity.create(referringObject);

        try {
            referencedEntity.checkForInboundRefs(referencedDao);
        } catch (RuntimeException e) {
            assertThat(e).isInstanceOf(IllegalStateException.class);
        }
    }

    @Test
    public void checkForInboundRefs_detectsInboundRefsFromListAnno() {
        // create and save a ReferencedDao entity
        VroomEntity<TestEntityObject, ReferencedDao2> referencedEntity = new VroomEntity<TestEntityObject,
                ReferencedDao2>(TestEntityObject.class, ReferencedDao2.class) {
        };
        referencedEntity.create(mTestEntityObject);
        ReferencedDao2 referencedDao = referencedEntity.loadDao(mTestEntityObject.getKey().get());


        // create and save a ReferringDao3 entity that refers to referencedDao's ID in a list field
        ReferringObject referringObject = new ReferringObject(mTestEntityObject.getKey());
        VroomEntity<ReferringObject, ReferringDao3> referringEntity2 =
                new VroomEntity<ReferringObject, ReferringDao3>(ReferringObject.class, ReferringDao3.class) {
                };
        referringEntity2.create(referringObject);

        try {
            referencedEntity.checkForInboundRefs(referencedDao);
        } catch (RuntimeException e) {
            assertThat(e).isInstanceOf(IllegalStateException.class);
        }
    }

    public static class TestOfyService extends OfyService {
        static {
            ObjectifyService.factory().register(TestDao.class);
            ObjectifyService.factory().register(ReferencedDao1.class);
            ObjectifyService.factory().register(ReferencedDao2.class);
            ObjectifyService.factory().register(ReferringDao1.class);
            ObjectifyService.factory().register(ReferringDao2.class);
            ObjectifyService.factory().register(ReferringDao3.class);
        }
    }

    private static class TestEntityObject extends EntityObject {
        public TestEntityObject() {
            super(new LongKey((Long) null));
        }
    }

    private static class ReferringObject extends EntityObject {
        private LongKey testObjectKey;

        public ReferringObject(LongKey testObjectKey) {
            super(new LongKey((Long) null));
            this.testObjectKey = testObjectKey;
        }

        public LongKey getRefKey() {
            return testObjectKey;
        }
    }

    private static class TestEntity extends VroomEntity<TestEntityObject, TestDao> {
        protected TestEntity() {
            super(TestEntityObject.class, TestDao.class);
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

    @Entity
    @InboundRef(daoClass = ReferringDao1.class, fieldName = "ref")
    private static class ReferencedDao1 extends VroomDao<TestEntityObject> {
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

    @Entity
    private static class ReferringDao1 extends VroomDao<ReferringObject> {
        @Index private Ref<ReferencedDao1> ref;

        @Override
        public ReferringObject toDomainObject() {
            return null;
        }

        @Override
        public void fromDomainObject(final ReferringObject referringObject) {
            ref = Ref.create(Key.create(ReferencedDao1.class, referringObject.getRefKey().get()));
        }
    }

    @Entity
    @InboundRefs({@InboundRef(daoClass = ReferringDao2.class, fieldName = "fooRef"),
            @InboundRef(daoClass = ReferringDao3.class, fieldName = "listOfRefs")})
    private static class ReferencedDao2 extends VroomDao<TestEntityObject> {
        @Override
        public TestEntityObject toDomainObject() {
            return null;
        }

        @Override
        public void fromDomainObject(final TestEntityObject entityObject) {
        }
    }

    // ReferringDao2 and ReferringDao3 both hold references to ReferencedDao2, but with different field names
    @Entity
    private static class ReferringDao2 extends VroomDao<ReferringObject> {
        @Index private Ref<ReferencedDao2> fooRef;

        @Override
        public ReferringObject toDomainObject() {
            return null;
        }

        @Override
        public void fromDomainObject(final ReferringObject referringObject) {
            fooRef = Ref.create(Key.create(ReferencedDao2.class, referringObject.getRefKey().get()));
        }
    }

    @Entity
    private static class ReferringDao3 extends VroomDao<ReferringObject> {
        @Index private List<Ref<ReferencedDao2>> listOfRefs;

        @Override
        public ReferringObject toDomainObject() {
            return null;
        }

        @Override
        public void fromDomainObject(final ReferringObject referringObject) {
            listOfRefs = new ArrayList<>();
            listOfRefs.add(Ref.create(Key.create(ReferencedDao2.class, referringObject.getRefKey().get())));
        }
    }
}
