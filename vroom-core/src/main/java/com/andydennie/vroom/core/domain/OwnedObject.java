package com.andydennie.vroom.core.domain;

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

public class OwnedObject<KT extends KeyType> extends KeyedObject<KT> {
    private KT mParentKey;

    public OwnedObject(final KT key, final KT parentKey) {
        super(key);
        mParentKey = parentKey;
    }

    public KT getParentKey() {
        return mParentKey;
    }
}
