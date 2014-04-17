package com.fizzbuzz.vroom.core.service.datastore;

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

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.List;


/**
 * A JUnit rule that implements setup and teardown of a GAE LocalServiceTestHelper.
 */
public class GaeRule extends ExternalResource {
    private LocalServiceTestHelper mHelper;

    private GaeRule(LocalServiceTestConfig... configs) {
        mHelper = new LocalServiceTestHelper(configs);
    }

    @Override
    protected void before() throws Throwable {
        mHelper.setUp();
    }

    @Override
    protected void after() {
        mHelper.tearDown();
    }

    public static class Builder {
        private boolean mWithDatastore = false;
        private boolean mWithBlobstore = false;
        private boolean mWithTaskQueue = false;
        private boolean mWithMemcache = false;
        private int mUnappliedPct = 0;

        public Builder withDatastore() {
            mWithDatastore = true;
            return this;
        }
        public Builder setUnappliedJobPct(final int pct) {
            mUnappliedPct = pct;
            return this;
        }
        public Builder withBlobstore() {
            mWithBlobstore = true;
            return this;
        }
        public Builder withTaskQueue() {
            mWithTaskQueue = true;
            return this;
        }
        public Builder withMemcache() {
            mWithMemcache = true;
            return this;
        }
        public GaeRule build() {
            List<LocalServiceTestConfig> configs = new ArrayList<>();
            if (mWithDatastore)
                configs.add(new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage
                        (mUnappliedPct));
            if (mWithMemcache)
                configs.add(new LocalMemcacheServiceTestConfig());
            if (mWithBlobstore)
                configs.add(new LocalBlobstoreServiceTestConfig());
            if (mWithTaskQueue)
                configs.add(new LocalTaskQueueTestConfig());
            return new GaeRule((configs.toArray(new LocalServiceTestConfig[configs.size()])));
        }
    }
}

