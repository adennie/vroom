package com.fizzbuzz.vroom.core.resource;

import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.exception.ConflictException;
import com.fizzbuzz.vroom.core.exception.InvalidResourceUriException;
import com.fizzbuzz.vroom.core.exception.NotFoundException;
import org.restlet.data.Status;
import org.restlet.resource.Options;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public abstract class BaseResource<D extends DomainObject> extends ServerResource {
    private final Logger mLogger = LoggerFactory.getLogger(PackageLogger.TAG);

    @Options
    public void doOptions() {
    }

    protected void doCatch(final RuntimeException e) {
        Class<?> exceptionClass = e.getClass();
        if (IllegalArgumentException.class.isAssignableFrom(exceptionClass)
                || InvalidResourceUriException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 400
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage(), e);
        }
        if (NotFoundException.class.isAssignableFrom(exceptionClass)
                || IndexOutOfBoundsException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 404
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getMessage(), e);
        }
        if (UnsupportedOperationException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 405
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, e.getMessage(), e);
        }
        if (ConflictException.class.isAssignableFrom(exceptionClass)
                || IllegalStateException.class.isAssignableFrom(exceptionClass)) {
            // chain & translate to a 409
            mLogger.error(e.getMessage());
            throw new ResourceException(Status.CLIENT_ERROR_CONFLICT, e.getMessage(), e);
        }

        throw e;
    }

    protected long getLongTokenValue(final String token) {

        String s = (String) getRequestAttributes().get(token);

        if (s == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "missing " + token + " URL component: " + s);
        }

        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "invalid " + token + " URL component: " + s);
        }
    }

    protected long getLongParamValue(final String paramName) {
        String paramAsString = null;

        Map<String, String> params = getQuery().getValuesMap();
        paramAsString = params.get(paramName);
        return Long.parseLong(paramAsString);
    }

    protected String getStringParamValue(final String paramName) {
        Map<String, String> params = getQuery().getValuesMap();
        return params.get(paramName);

    }
}
