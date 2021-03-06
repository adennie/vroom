package com.fizzbuzz.vroom.core.api.dto_converter;

/*
 * Copyright (c) 2013 Fizz Buzz LLC
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

import com.fizzbuzz.vroom.dto.HttpMethod;
import com.fizzbuzz.vroom.dto.VroomDto;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import java.io.IOException;
import java.util.List;

public abstract class VroomConverter<DTO extends VroomDto, O extends Object>
        extends ConverterHelper {

    private final Class<O> mObjectClass;
    private final Class<DTO> mDtoClass;
    private final MediaType[] mSupportedMediaTypes;

    public VroomConverter(Class<DTO> dtoClass,
                          Class<O> objectClass,
                          MediaType... supportedMediaTypes) {
        mObjectClass = objectClass;
        mDtoClass = dtoClass;
        mSupportedMediaTypes = supportedMediaTypes;
    }

    // returns the list of classes that this converter can produce instances of, from a representation with the
    // specified media type
    @Override
    public List<Class<?>> getObjectClasses(final Variant source) {
        List<Class<?>> result = null;

        for (MediaType mediaType : mSupportedMediaTypes) {
            if (mediaType.equals(source.getMediaType())) {
                result = addObjectClass(result, mObjectClass);
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
                (mObjectClass.isAssignableFrom(source)
                        || source.isAssignableFrom(mObjectClass))) {
            for (MediaType mediaType : mSupportedMediaTypes) {
                result = addVariant(result, new VariantInfo(mediaType));
            }
        }

        return result;
    }

    @Override
    public <DO> float score(final Representation source,
                            final Class<DO> target,
                            final Resource resource) {
        // Competition between currently registered converters to produce an object of the target class, from a
        // representation having the specified media type (e.g. for PUT or POST input payloads)
        float result = -1.0F;

        if (mObjectClass.isAssignableFrom(target) || target.isAssignableFrom(mObjectClass)) {
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

        if (source != null && mObjectClass.isAssignableFrom(source.getClass())) {
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
    public <DO> DO toObject(final Representation source,
                            final Class<DO> target,
                            final Resource resource) throws IOException {
        DO result = null;

        // Convert from JSON to DTO
        JacksonRepresentation<?> jacksonSource = new JacksonRepresentation<DTO>(source, mDtoClass);
        DTO dto = (DTO) jacksonSource.getObject();

        // validate DTO
        validateDto(dto, resource.getMethod());

        // convert from DTO to DO
        result = (DO) toObject(dto);
        return result;
    }

    @Override
    public Representation toRepresentation(final Object source,
                                           final Variant target,
                                           final Resource resource) throws IOException {

        DTO dto = toDto((O) source);

        // create a JacksonRepresentation
        JacksonRepresentation<?> jacksonRep = new JacksonRepresentation<DTO>(target.getMediaType(),
                (DTO) dto);

        return jacksonRep;
    }

    protected void validateDto(final DTO dto, final Method method) {
        HttpMethod httpMethod = null;
        // translate from Restlet Method datatype to Vroom method enum
        if (method.equals(Method.GET))
            httpMethod = HttpMethod.GET;
        else if (method.equals(Method.PUT))
            httpMethod = HttpMethod.PUT;
        else if (method.equals(Method.POST))
            httpMethod = HttpMethod.POST;
        else if (method.equals(Method.DELETE))
            httpMethod = HttpMethod.DELETE;
        else if (method.equals(Method.OPTIONS))
            httpMethod = HttpMethod.OPTIONS;

        dto.validate(httpMethod);
    }

    protected abstract DTO toDto(final O domainObject);
    protected abstract O toObject(final DTO dto);
}
