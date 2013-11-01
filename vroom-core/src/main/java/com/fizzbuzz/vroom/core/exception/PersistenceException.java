package com.fizzbuzz.vroom.core.exception;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersistenceException() {
        super("no message provided by caller");
    }

    public PersistenceException(final String message) {
        super(message);
    }

    public PersistenceException(final String message,
                                final Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(final Throwable cause) {
        super(cause);
    }
}
