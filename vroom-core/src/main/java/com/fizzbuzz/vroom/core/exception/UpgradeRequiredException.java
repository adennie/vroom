package com.fizzbuzz.vroom.core.exception;

public class UpgradeRequiredException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UpgradeRequiredException(final String message) {
        super(message);
    }

    public UpgradeRequiredException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UpgradeRequiredException(final Throwable cause) {
        super(cause);
    }
}
