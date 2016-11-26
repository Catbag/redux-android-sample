package br.com.catbag.gifreduxsample.reducers.reducers;

import com.umaplay.fluxxan.Action;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import br.com.catbag.gifreduxsample.BuildConfig;
import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.actions.AppStateActionCreator;
import br.com.catbag.gifreduxsample.models.AppState;
import shared.ReduxBaseTest;
import shared.TestHelper;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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
// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class AppStateReducerTest extends ReduxBaseTest {

    public AppStateReducerTest() {
        mHelper = new TestHelper(MyApp.getFluxxan());
        mHelper.clearMiddlewares();
    }

    @Test
    public void initialAppState() throws Exception {
        assertTrue(mHelper.getFluxxan().getState().getGifs().isEmpty());
        assertTrue(mHelper.getFluxxan().getState().getHasMoreGifs());
    }

    @Test
    public void whenSendAppStateLoaded() {
        AppState expectedAppState = TestHelper.buildAppState(TestHelper.buildFiveGifs());
        mHelper.dispatchAction(new Action(AppStateActionCreator.APP_STATE_LOADED,
                expectedAppState));
        assertEquals(expectedAppState, mHelper.getFluxxan().getState());
    }

}
