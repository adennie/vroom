package com.fizzbuzz.vroom.core.exception;

public class ConflictException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConflictException(final String conflictingEntity, final String conflictingProperty,
            final String conflictingValue) {
        super(generateMessage(conflictingEntity, conflictingProperty, conflictingValue));
    }

    public ConflictException(final String conflictingEntity, final String conflictingProperty,
            final String conflictingValue, final Throwable cause) {
        super(generateMessage(conflictingEntity, conflictingProperty, conflictingValue), cause);
    }

    public ConflictException(final Throwable cause) {
        super(cause);
    }

    private static String generateMessage(final String conflictingEntity, final String conflictingProperty,
            final String conflictingValue) {
        return conflictingEntity + " conflict encountered.  Conficting property: \"" + conflictingProperty
                + "\", conflicting value: \"" + conflictingValue + "\".";
    }
}
