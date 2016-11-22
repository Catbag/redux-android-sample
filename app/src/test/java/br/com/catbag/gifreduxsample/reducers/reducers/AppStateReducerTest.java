package br.com.catbag.gifreduxsample.reducers.reducers;

import com.umaplay.fluxxan.Action;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
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
 * Created by felipe on 13/11/16.
 */
// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class AppStateReducerTest extends ReduxBaseTest {

    public AppStateReducerTest() {
        mHelper = new TestHelper(((MyApp) RuntimeEnvironment.application).getFluxxan());
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
