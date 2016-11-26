package shared;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

/**
 Copyright 26/10/2016
 Felipe Piñeiro (fpbitencourt@gmail.com),
 Nilton Vasques (nilton.vasques@gmail.com) and
 Raul Abreu (raulccabreu@gmail.com)

 Be free to ask for help, email us!

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/

public class ReduxBaseTest {
    @Rule
    public Timeout mGlobalTimeout = new Timeout(1, TimeUnit.MINUTES);

    protected TestHelper mHelper = null;

    @Before
    public void setup() {
        if (mHelper != null) {
            mHelper.getFluxxan().registerReducer(new FakeReducer());
            mHelper.activateStateListener();
        } else {
            informTestHelperNotInitialized();
        }
    }

    @After
    public void cleanup() {
        if (mHelper != null) {
            mHelper.deactivateStateListener();
            mHelper.getFluxxan().unregisterReducer(FakeReducer.class);
        } else {
            informTestHelperNotInitialized();
        }
    }

    private void informTestHelperNotInitialized() {
        Log.e(getClass().getSimpleName(),
                "TestHelper not initialized: set super.mHelper with properly Fluxxan instance");
    }
}
