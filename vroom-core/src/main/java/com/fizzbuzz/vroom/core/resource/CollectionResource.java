package com.fizzbuzz.vroom.core.resource;

import com.fizzbuzz.vroom.core.domain.DomainCollection;
import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.dto_adapter.CollectionAdapter;
import com.fizzbuzz.vroom.core.dto_adapter.ObjectAdapter;
import com.fizzbuzz.vroom.dto.CollectionDto;
import com.fizzbuzz.vroom.dto.ObjectDto;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

public abstract class CollectionResource<
        DTC extends CollectionDto<DTO>,
        DTO extends ObjectDto,
        DC extends DomainCollection<DO>,
        DO extends DomainObject>
        extends BaseResource<DO> {
    private DC mDomainCollection;
    private CollectionAdapter<DTC, DTO, DC, DO> mCollectionAdapter;
    private ObjectAdapter<DTO, DO> mElementAdapter;

    @Get("json")
    public DTC getResource() {
        DTC result = null;
        try {
            result = mCollectionAdapter.toDto(mDomainCollection);
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Post("json:json")
    public DTO postResource(final DTO elementDto) {
        DTO result = null;
        try {
            // when creating a new resource, the DTO's uri field must be blank
            if (!elementDto.getUri().isEmpty()) {
                throw new IllegalArgumentException("when creating a new resource, the value of the uri field must be " +
                        "a blank string");
            }
            DO domainObject = (getDomainCollection())
                    .add(mElementAdapter.toDomain(elementDto));
            result = mElementAdapter.toDto(domainObject);
            getResponse().setStatus(Status.SUCCESS_CREATED);
            getResponse().setLocationRef(mElementAdapter.getCanonicalUri(domainObject));
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    @Delete
    public void deleteResource() {
        (getDomainCollection()).deleteAll();
    }

    @Override
    public Representation toRepresentation(final Object source,
                                           final Variant target) {
        Representation result = super.toRepresentation(source, target);
        // the POST method creates a new collection element, which is returned as the response body. We should
        // specify the
        // Content-Location header to indicate the URI of that resource. The value of the URI was already stored into
        // the LocationRef of the response, so just grab that and reuse it.
        if (getMethod().equals(Method.POST))
            result.setLocationRef(getResponse().getLocationRef());
        return result;
    }

    protected void doInit(final DC domainCollection,
                          final CollectionAdapter<DTC, DTO, DC, DO> collectionAdapter,
                          final ObjectAdapter<DTO, DO> elementAdapter) throws ResourceException {
        mDomainCollection = domainCollection;
        mCollectionAdapter = collectionAdapter;
        mElementAdapter = elementAdapter;
    }

    protected CollectionAdapter<DTC, DTO, DC, DO> getCollectionAdapter() {
        return mCollectionAdapter;
    }

    protected DomainCollection<DO> getDomainCollection() {
        return mDomainCollection;
    }
}
