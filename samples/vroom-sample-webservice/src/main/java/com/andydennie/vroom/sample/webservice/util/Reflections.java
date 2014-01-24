package com.andydennie.vroom.sample.webservice.util;

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

import java.lang.reflect.Constructor;

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
     * @param arg1      the value to pass as the constructor's 1st parameter
     * @param arg2      the value to pass as the constructor's 2nd parameter
     * @param <T>         the type of the object to be instantiated
     * @param <P1>        the type of the 1st parameter of the target constructor
     * @param <P2>        the type of the 1st parameter of the target constructor
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
     * @return      an instance of the requested class
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
     * @return      an instance of the requested class
     */
    public static <T> T newInstance(final Class<T> clazz) {
        T result = null;

        try {
            result = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("failed to instantiate class: " + clazz.getName());
        }
        return result;
    }

    /**
     * Instantiates an object of a specified class using the provided constructor and argument list.
     *
     * @param ctor  the constructor to invoke
     * @param args  the arguments to pass to the constructor
     * @param <T>   the type of the object to be instantiated
     * @return      an instance of the requested class
     */
    public static <T> T newInstance(final Constructor<T> ctor,
                                    final Object... args) {
        T result = null;

        try {
            result = ctor.newInstance(args);
        }
        catch (Exception e) {
            throw new RuntimeException("failed to instantiate class: " + ctor.getClass().getName(), e);

        }
        return result;
    }

    /**
     * Looks up the constructor of a given class having the signature specified by a list of parameter types
     *
     * @param clazz       a Class object for the target type
     * @param paramTypes  the target constructor's parameter types
     * @param <T>         the target type
     * @return            the constructor matching the requested signature
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

    // this is just a collection of static methods. Make the constructor private to prevent instantiation.
    private Reflections() {
    }

}
