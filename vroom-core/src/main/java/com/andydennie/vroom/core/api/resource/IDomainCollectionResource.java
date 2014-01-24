package com.andydennie.vroom.core.api.resource;

/*
 * Copyright (c) 2014 Andy Dennie
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

import com.andydennie.vroom.core.domain.DomainCollection;
import com.andydennie.vroom.core.domain.IDomainObject;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;

public interface IDomainCollectionResource<
        DC extends DomainCollection<DO>,
        DO extends IDomainObject>
        extends IVroomResource {

    public DomainCollection<DO> getResource();

    public DO postResource(final DO element);

    public void deleteResource();

    public Representation toRepresentation(final Object source, final Variant target);

}
