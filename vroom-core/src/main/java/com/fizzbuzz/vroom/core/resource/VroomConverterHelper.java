package com.fizzbuzz.vroom.core.resource;

import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import java.io.IOException;
import java.util.List;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class VroomConverterHelper<R>
        extends ConverterHelper {

    private final MediaType[] mSupportedMediaTypes;
    private final Class<?> mTargetResourceClass;

    public VroomConverterHelper(Class<?> targetResourceClass,
                                MediaType... supportedMediaTypes) {
        mTargetResourceClass = targetResourceClass;
        mSupportedMediaTypes = supportedMediaTypes;
    }

    // returns the list of classes that this converter can produce instances of, from a representation with the
    // specified media type
    @Override
    public List<Class<?>> getObjectClasses(final Variant source) {
        List<Class<?>> result = null;

        for (MediaType mediaType : mSupportedMediaTypes) {
            if (mediaType.equals(source.getMediaType())) {
                result = addObjectClass(result, mTargetResourceClass);
            }
        }

        return result;
    }

    // returns the media types of representations that this converter can produce from the specified object class
    @Override
    public List<VariantInfo> getVariants(final Class<?> source) {
        List<VariantInfo> result = null;

        // return this converter's collection of supported media types if
        // (1) the target class is assignable from the source class (i.e. the target class same as,
        // or a base class of, the source class)
        // (2) the source class is assignable from the base class (i.e. the target class is the same as,
        // or a derived class of, the base class)
        // Case (2) looks odd, but it handles the situation where the annotated interface specifies a polymorphic
        // parameter type, where the actual
        // objects passed are derived from that class.
        if (source != null &&
                (mTargetResourceClass.isAssignableFrom(source)
                        || source.isAssignableFrom(mTargetResourceClass))) {
            for (MediaType mediaType : mSupportedMediaTypes) {
                result = addVariant(result, new VariantInfo(mediaType));
            }
        }

        return result;
    }

    @Override
    public <T> float score(final Representation source,
                           final Class<T> target,
                           final Resource resource) {
        // Competition between currently registered converters to produce an object of the target class, from a
        // representation having the specified media type (e.g. for PUT or POST input payloads)
        float result = -1.0F;

        if (mTargetResourceClass.isAssignableFrom(target) || target.isAssignableFrom(mTargetResourceClass)) {
            for (MediaType mediaType : mSupportedMediaTypes) {
                if (mediaType.equals(source.getMediaType()))
                    result = 1.0F;
            }
        }
        return result;
    }

    @Override
    public float score(final Object source,
                       final Variant target,
                       final Resource resource) {
        // Competition between currently registered converters to produce the target representation for
        // toRepresentation()
        float result = -1.0F;

        if (source != null && mTargetResourceClass.isAssignableFrom(source.getClass())) {
            if (target == null) {
                result = 1.0F; // no target type specified, but we match on the source, so we're probably the best match
            } else { // target type specified. Return 1.0F if we can match it.
                for (MediaType mediaType : mSupportedMediaTypes) {
                    if (mediaType.equals(target.getMediaType()))
                        result = 1.0F;
                }
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(final Representation source,
                          final Class<T> target,
                          final Resource resource) throws IOException {
        T result = null;

        // Convert from JSON to T
        JacksonRepresentation<?> jacksonSource = new JacksonRepresentation<T>(source, target);
        result = (T) jacksonSource.getObject();

        return result;
    }

    @Override
    public Representation toRepresentation(final Object source,
                                           final Variant target,
                                           final Resource resource) throws IOException {

        // create a JacksonRepresentation and tell it to use the JsonView appropriate to the target version
        JacksonRepresentation<?> jacksonRep = new JacksonRepresentation<R>(target.getMediaType(),
                (R) source);

        return jacksonRep;
    }
}
