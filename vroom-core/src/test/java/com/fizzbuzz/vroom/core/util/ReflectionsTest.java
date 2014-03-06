package com.fizzbuzz.vroom.core.util;

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

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Date;
import static org.fest.assertions.api.Assertions.*;
public class ReflectionsTest {
    @Test
    public void testGetFirstFieldAnnotatedWith1() {
        // look for field annotated with @TestAnno in BaseClass; should find "baseDate"
        Field field = Reflections.getFirstFieldAnnotatedWith(BaseClass.class, Object.class, TestAnno.class);
        assertThat(field).isNotNull();
        assertThat(field.getAnnotation(TestAnno.class)).isNotNull();
        assertThat(field.getName().equals("baseDate"));
    }

    @Test
    public void testGetFirstFieldAnnotatedWith2() {
        // look for field annotated with @Test in BaseClass; shouldn't find one
        Field field = Reflections.getFirstFieldAnnotatedWith(BaseClass.class, Object.class, Test.class);
        assertThat(field).isNull();
    }

    @Test
    public void testGetFirstFieldAnnotatedWith3() {
        // look for field annotated with @TestAnno in DerivedClass; should find "derivedDate"
        Field field = Reflections.getFirstFieldAnnotatedWith(BaseClass.class, Object.class, TestAnno.class);
        assertThat(field).isNotNull();
        assertThat(field.getAnnotation(TestAnno.class)).isNotNull();
        assertThat(field.getName().equals("derivedDate"));
    }

    public static class BaseClass {
        @TestAnno private Date baseDate;
    }

    public static class DerivedClass extends BaseClass {
        @TestAnno private Date derivedDate;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface TestAnno {
    }

}
