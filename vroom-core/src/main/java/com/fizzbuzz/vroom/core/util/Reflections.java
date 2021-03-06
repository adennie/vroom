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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Reflections {

    /**
     * Instantiates an object of a specified class using a constructor with 1 parameter of a specified type.
     *
     * @param clazz      a Class object corresponding to the class of the object to be instantiated
     * @param paramClass a Class object corresponding to the type of the (only) parameter of the target class'
     *                   target constructor
     * @param param      the value to pass as the constructor's only parameter
     * @param <T>        the type of the object to be instantiated
     * @param <P>        the type of the (only) parameter of the target constructor
     * @return an instance of the requested type
     */
    public static <T, P> T newInstance(final Class<T> clazz,
                                       final Class<P> paramClass,
                                       final P param) {
        // get the constructor for the specified class that takes a single param of type P. Then construct it.
        Constructor<T> ctor = getConstructor(clazz, new Class[]{paramClass});
        return newInstance(ctor, param);
    }

    /**
     * Instantiates an object of a specified class using a constructor with 2 parameters of specified types.
     *
     * @param clazz       a Class object corresponding to the class of the object to be instantiated
     * @param param1Class a Class object corresponding to the type of the 1st parameter of the target class'
     *                    target constructor
     * @param param2Class a Class object corresponding to the type of the 2nd parameter of the target class'
     *                    target constructor
     * @param arg1        the value to pass as the constructor's 1st parameter
     * @param arg2        the value to pass as the constructor's 2nd parameter
     * @param <T>         the type of the object to be instantiated
     * @param <P1>        the type of the 1st parameter of the target constructor
     * @param <P2>        the type of the 1st parameter of the target constructor
     * @param <A1>        the type of the value to supply for the 1st parameter of the target constructor
     * @param <A2>        the type of the value to supply for the 2nd parameter of the target constructor
     * @return an instance of the requested class
     */

    public static <T, P1, P2, A1 extends P1, A2 extends P2> T newInstance(final Class<T> clazz,
                                                                          final Class<P1> param1Class,
                                                                          final Class<P2> param2Class,
                                                                          final A1 arg1,
                                                                          final A2 arg2) {
    /*
    public static <T, P1, P2> T newInstance(final Class<T> clazz,
                                                                          final Class<P1> param1Class,
                                                                          final Class<P2> param2Class,
                                                                          final P1 arg1,
                                                                          final P2 arg2) {
    */
        // get the constructor for the specified class that takes a two params, the 1st of type P1 and the second of
        // type P2. Then construct it.
        Constructor<T> ctor = getConstructor(clazz, new Class[]{param1Class, param2Class});
        return newInstance(ctor, arg1, arg2);
    }

    /**
     * Instantiates an object of a specified class using a constructor with a single long parameter.
     *
     * @param clazz a Class object corresponding to the class of the object to be instantiated
     * @param param the parameter value to pass to the constructor
     * @param <T>   the type of the object to be instantiated
     * @return an instance of the requested class
     */
    public static <T> T newInstance(final Class<T> clazz,
                                    final long param) {
        // get the constructor for the specified class that takes a long param. Then construct it.
        Constructor<T> ctor = getConstructor(clazz, new Class[]{long.class});
        return newInstance(ctor, param);
    }

    /**
     * Instantiates an object of a specified class using a no-arg constructor.
     *
     * @param clazz a Class object corresponding to the class of the object to be instantiated
     * @param <T>   the type of the object to be instantiated
     * @return an instance of the requested class
     */
    public static <T> T newInstance(final Class<T> clazz) {
        T result = null;

        try {
            Constructor<T> ctor = getDefaultConstructor(clazz);
            ctor.setAccessible(true);
            result = ctor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("failed to instantiate class: " + clazz.getName());
        }
        return result;
    }

    /**
     * Instantiates an object of a specified class using the provided constructor and argument list.
     *
     * @param ctor the constructor to invoke
     * @param args the arguments to pass to the constructor
     * @param <T>  the type of the object to be instantiated
     * @return an instance of the requested class
     */
    public static <T> T newInstance(final Constructor<T> ctor,
                                    final Object... args) {
        T result = null;

        try {
            ctor.setAccessible(true);
            result = ctor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("failed to instantiate class: " + ctor.getClass().getName(), e);

        }
        return result;
    }

    /**
     * Looks up the constructor of a given class having the signature specified by a list of parameter types
     *
     * @param clazz      a Class object for the target type
     * @param paramTypes the target constructor's parameter types
     * @param <T>        the target type
     * @return the constructor matching the requested signature
     */
    public static <T> Constructor<T> getConstructor(final Class<T> clazz,
                                                    final Class<?>... paramTypes) {
        Constructor<T> result = null;
        try {
            result = clazz.getConstructor(paramTypes);
        } catch (Exception e) {
            throw new RuntimeException("failed to get constructor for class: " + clazz.getName());

        }
        return result;

    }

    /**
     * @param clazz a Class object for the target type
     * @param <T>   the target type
     * @return the default constructor for clazz, or null if one does not exist
     */
    public static <T> Constructor<T> getDefaultConstructor(final Class<T> clazz) {
        Constructor[] ctors = clazz.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        return ctor;
    }

    /**
     * Returns a list of fields in a class hierarchy having a specified annotation.
     *
     * @param startClass a class whose fields (and whose superclasses' fields) are to be retrieved
     * @param stopClass  a class in the superclass hierarchy of startClass whose fields should not be retrieved
     *                   (nor its superclasses' fields)
     * @param annotationClass the annotation to look for
     * @return the list of fields
     */
    public static List<Field> getFieldsAnnotatedWith(Class<?> startClass,
                                                     Class<?> stopClass,
                                                     Class<? extends Annotation> annotationClass) {

        List<Field> result = new ArrayList<>();
        Class<?> targetClass = startClass;
        while (targetClass != null &&
                (stopClass == null || !(targetClass.equals(stopClass)))) {
            Field[] fields = targetClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getAnnotation(annotationClass) != null) {
                    result.add(fields[i]);
                }
            }
            targetClass = targetClass.getSuperclass();
        }

        return result;
    }

    /**
     * Returns the first field found in a class hierarchy having a specified annotation.
     *
     * @param startClass a class whose fields (and whose superclasses' fields) are to be inspected
     * @param stopClass  a class in the superclass hierarchy of startClass whose fields should not be inspected
     *                   (nor its superclasses' fields)
     * @param annotationClass the annotation to look for
     * @return the first field found with the specified annotation, or null if none found.
     */
    public static Field getFirstFieldAnnotatedWith(Class<?> startClass,
                                                   Class<?> stopClass,
                                                   Class<? extends Annotation> annotationClass) {

        Field result = null;
        Class<?> targetClass = startClass;

        while (targetClass != null &&
                (stopClass == null || !(targetClass.equals(stopClass)))) {
            Field[] fields = targetClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getAnnotation(annotationClass) != null) {
                    result = fields[i];
                    break;
                }
            }
            if (result != null) // already found it?
                break;

            targetClass = targetClass.getSuperclass();
        }

        return result;
    }

    /**
     * Returns a collection of type annotations found in a class hierarchy
     *
     * @param startClass      a class whose type annotations (and whose superclasses' type annotations) are to be
     *                        inspected
     * @param stopClass       a class in the superclass hierarchy of startClass whose type annotations should not be
     *                        inspected (nor its superclasses')
     * @param annotationClass the annotation class to look for
     * @param <T>             the type of the annotation class to look for
     * @return                the collection of type annotations found
     */
    public static <T extends Annotation> Collection<T> getClassAnnotations(Class<?> startClass,
                                                                           Class<?> stopClass,
                                                                           Class<T> annotationClass) {

        List<T> result = new ArrayList<>();
        Class<?> targetClass = startClass;

        while (targetClass != null &&
                (stopClass == null || !(targetClass.equals(stopClass)))) {
            T anno = targetClass.getAnnotation(annotationClass);
            if (anno != null)
                result.add(anno);

            targetClass = targetClass.getSuperclass();
        }

        return result;
    }

    // this is just a collection of static methods. Make the constructor private to prevent instantiation.
    private Reflections() {
    }
}
