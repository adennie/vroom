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
import java.util.Collection;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
public class ReflectionsTest {
    @Test
    public void getFirstFieldAnnotatedWith_findsFieldWithAnno() {
        // look for field annotated with @TestFieldAnno in BaseClass; should find "baseDate"
        Field field = Reflections.getFirstFieldAnnotatedWith(BaseClass.class, Object.class, TestFieldAnno.class);
        assertThat(field).isNotNull();
        assertThat(field.getAnnotation(TestFieldAnno.class)).isNotNull();
        assertThat(field.getName().equals("baseDate"));
    }

    @Test
    public void getFirstFieldAnnotatedWith_returnsNullForAnnoNotPresent() {
        // look for field annotated with @Test in BaseClass; shouldn't find one
        Field field = Reflections.getFirstFieldAnnotatedWith(BaseClass.class, Object.class, Test.class);
        assertThat(field).isNull();
    }

    @Test
    public void getFirstFieldAnnotatedWith_findsFieldWithAnnoInDerivedClass() {
        // look for field annotated with @TestFieldAnno in DerivedClass; should find "derivedDate"
        Field field = Reflections.getFirstFieldAnnotatedWith(DerivedClass.class, Object.class, TestFieldAnno.class);
        assertThat(field).isNotNull();
        assertThat(field.getAnnotation(TestFieldAnno.class)).isNotNull();
        assertThat(field.getName()).isEqualTo("derivedDate");
    }

    @Test
    public void getClassAnnotations_findsAnnoOnBaseClass() {
        Collection<TestTypeAnno> annos =
                Reflections.getClassAnnotations(BaseClass.class, Object.class, TestTypeAnno.class);
        assertThat(annos.size()).isEqualTo(1);
        for (TestTypeAnno anno : annos) {
            assertThat(anno.string()).isEqualTo("base");
        }
    }

    @Test
    public void getClassAnnotations_findsAnnosOnBaseAndDerivedClasses() {
        Collection<TestTypeAnno> annos =
                Reflections.getClassAnnotations(DerivedClass.class, Object.class, TestTypeAnno.class);
        assertThat(annos.size()).isEqualTo(2);
        boolean foundBaseAnno = false;
        boolean foundDerivedAnno = false;
        for (TestTypeAnno anno : annos) {
            if (anno.string().equals("base"))
                foundBaseAnno = true;
            if (anno.string().equals("derived"))
                foundDerivedAnno = true;
        }
        assertThat(foundBaseAnno).isTrue();
        assertThat(foundDerivedAnno).isTrue();
    }

    @TestTypeAnno(string="base")
    public static class BaseClass {
        @TestFieldAnno private Date baseDate;
    }

    @TestTypeAnno(string="derived")
    public static class DerivedClass extends BaseClass {
        @TestFieldAnno private Date derivedDate;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface TestFieldAnno {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface TestTypeAnno {
        String string();
    }
}
