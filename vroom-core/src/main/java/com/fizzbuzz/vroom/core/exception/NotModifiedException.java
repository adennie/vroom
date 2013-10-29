package com.fizzbuzz.vroom.core.exception;

public class NotModifiedException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotModifiedException(final String message) {
        super(message);
    }

    public NotModifiedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotModifiedException(final Throwable cause) {
        super(cause);
    }
}
