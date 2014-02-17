package com.fizzbuzz.vroom.core.biz;

/*
 * Copyright (c) 2014 Fizz Buzz LLC
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

import com.fizzbuzz.vroom.core.domain.IEntityObject;
import com.fizzbuzz.vroom.core.domain.LongKey;
import com.fizzbuzz.vroom.core.service.datastore.IEntity;

public interface IEntityBiz<E extends IEntityObject> extends IKeyedObjectBiz<E, LongKey>{
    public IEntity<E> getEntity();

    public E get(final LongKey key);

    public E get(final String keyString);

    public void update(final E entityObject);

    public void delete(final LongKey key);

    public void delete(final String keyString);
}
