package com.fizzbuzz.vroom.core.exception;

public class InvalidResourceUriException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidResourceUriException() {
        super("no message provided by caller");
    }

    public InvalidResourceUriException(final String invalidUrl) {
        super(generateMessage(invalidUrl));
    }

    public InvalidResourceUriException(final String invalidUrl,
                                       final Throwable cause) {
        super(generateMessage(invalidUrl), cause);
    }

    public InvalidResourceUriException(final Throwable cause) {
        super(cause);
    }

    private static String generateMessage(final String invalidUri) {
        return "Not a valid resource URI: " + invalidUri;
    }
}
