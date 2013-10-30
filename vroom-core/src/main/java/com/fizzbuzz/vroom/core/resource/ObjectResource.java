package com.fizzbuzz.vroom.core.resource;

import com.fizzbuzz.vroom.core.domain.DomainObject;
import com.fizzbuzz.vroom.core.dto_adapter.ObjectAdapter;
import com.fizzbuzz.vroom.core.persist.ObjectPersist;
import com.fizzbuzz.vroom.dto.ObjectDto;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ResourceException;

/*
 * Base server resource class for objects with IDs (fetched from the URL).
 */
public abstract class ObjectResource<
        DTO extends ObjectDto,
        P extends ObjectPersist<DO>,
        DO extends DomainObject>
        extends BaseResource<DO> {
    private long mId;
    private P mPersist;
    private ObjectAdapter<DTO, DO> mDtoAdapter;

    public DTO getResource() {
        DTO result = null;
        try {
            DO domainObject = mPersist.get(mId);
            result = mDtoAdapter.toDto(domainObject);
        } catch (RuntimeException e) {
            doCatch(e);
        }
        return result;
    }

    public void putResource(final DTO dto) {
        try {
            // by default, return 204, since we're not returning any representation. Subclasses that override
            // putResource() can change the response status if needed.
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

            // make sure the client isn't trying to PUT a resource value with an ID that doesn't match the one
            // identified by the request URL.
            if (mDtoAdapter.getId(dto) != mId) {
                throw new IllegalArgumentException("The ID of the resource in the request body does not match the ID " +
                        "of the resource in the request URL");
            }

            mPersist.update(mDtoAdapter.toDomain(dto));
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    @Delete
    public void deleteResource() {
        try {
            mPersist.delete(mId);
        } catch (RuntimeException e) {
            doCatch(e);
        }
    }

    protected void doInit(final P persist,
                          final String idToken,
                          final ObjectAdapter<DTO, DO> dtoAdapter) throws ResourceException {

        super.doInit();

        mDtoAdapter = dtoAdapter;
        mPersist = persist;

        // get the object ID from the URL
        mId = getLongTokenValue(idToken);

    }

    protected long getId() {
        return mId;
    }
}
