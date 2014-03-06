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
import com.googlecode.objectify.annotation.Entity;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.fest.reflect.core.Reflection.field;

public class VroomDaoTest {
    private static Long TEST_ID = 123L;
    private VroomDao<EntityObject> mTestDao;

    @Before
    public void setup() {
        mTestDao = new TestDao();
    }

    @Test
    public void testGetId() throws Exception {
        // there's no setter for the "id" field, so set it via reflection
        field("id").ofType(Long.class).in(mTestDao).set(TEST_ID);
        assertThat(mTestDao.getId().equals(TEST_ID));
    }
/*
    @Test
    public void testAllocateIdThrowsWhenIdIsAlreadyAssigned() throws Exception {
        // there's no setter for the "id" field, so set it via reflection
        field("id").ofType(Long.class).in(mTestDao).set(TEST_ID);
        try {
            // the allocateId() method is protected, so invoke it via reflection
            method("allocateId").in(mTestDao).invoke();
        }
        catch (RuntimeException e) {
            assertThat(e.getClass().isAssignableFrom(IllegalStateException.class));
        }
    }
    @Test
    public void testAllocateIdSucceedsWhenIdIsNull() throws Exception {
        mTestDao.allocateId();
        assertThat(mTestDao.getId()).isNotNull();
    }
*/
    @Entity
    private static class TestDao extends VroomDao<EntityObject> {
        @Override
        public EntityObject toDomainObject() {
            return null;
        }

        @Override
        public void fromDomainObject(final EntityObject domainObject) {
        }
       }
}
