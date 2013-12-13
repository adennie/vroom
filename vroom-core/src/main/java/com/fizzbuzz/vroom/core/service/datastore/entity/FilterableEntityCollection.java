package com.fizzbuzz.vroom.core.service.datastore.entity;

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

import com.fizzbuzz.vroom.core.domain.KeyedObject;

import java.util.List;
import java.util.Map;

public interface FilterableEntityCollection <KO extends KeyedObject,
        FC> extends EntityCollection<KO> {
    public List<KO> getFilteredElements(final Map<FC, Object> constraints);
}
