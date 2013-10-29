package com.fizzbuzz.vroom.core.persist;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public abstract class PersistManager {
    private static OfyService mOfyService;

    public static void registerOfyService(final OfyService ofyService) {
        mOfyService = ofyService;
    }

    public static OfyService getOfyService() {
        if (mOfyService == null) {
            throw new IllegalStateException("no OfyService assigned");
        }
        return mOfyService;
    }

}
