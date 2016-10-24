package br.com.catbag.gifreduxsample.reducers;

import android.util.Log;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.StateListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import br.com.catbag.gifreduxsample.App;
import br.com.catbag.gifreduxsample.BuildConfig;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.models.AppState;

import static org.junit.Assert.assertEquals;

// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants=BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class GifReducerTest {
    private static final String TAG = "GifReducerTest";
    private Boolean stateChanged = false;

    @Before
    public void setup(){
        getApp().getFluxxan().getDispatcher().addListener(new StateListener() {
            @Override
            public boolean hasStateChanged(Object newState, Object oldState) {
                stateChanged = true;
                return newState != oldState;
            }
            @Override
            public void onStateChanged(Object o) { }
        });
    }

    @Test
    public void initialAppState() throws Exception {
        String expectedDefaultURL = "http://10.0.2.2:8000/goku.gif" ;
        assertEquals(AppState.GifStatus.NOT_DOWNLOADED, currentState().getGifStatus());
        assertEquals("", currentState().getGifLocalPath());
        assertEquals("goku", currentState().getGifTitle());
        assertEquals(expectedDefaultURL, currentState().getGifUrl());
        assertEquals(false, currentState().getGifWatched());
        assertEquals("", currentState().getGifDownloadFailureMsg());
    }

    @Test
    public void whenSendDownloadStartedAction() throws Exception {
        dispatchAction(new Action(GifActionCreator.GIF_DOWNLOAD_STARTED));
        assertEquals(AppState.GifStatus.DOWNLOADING, currentState().getGifStatus());
    }

    @Test
    public void whenSendDownloadSuccessAction() throws Exception {
        String expectedPath = "/test/image.gif";
        dispatchAction(new Action(GifActionCreator.GIF_DOWNLOAD_SUCCESS, expectedPath));
        assertEquals(expectedPath, currentState().getGifLocalPath());
        assertEquals(AppState.GifStatus.DOWNLOADED, currentState().getGifStatus());
    }

    @Test
    public void whenSendDownloadFailureAction() throws Exception {
        // dirting app state
        String expectedPath = "/test/image.gif";
        dispatchAction(new Action(GifActionCreator.GIF_DOWNLOAD_SUCCESS, expectedPath));

        String expectedErrorMsg = "failed";
        dispatchAction(new Action(GifActionCreator.GIF_DOWNLOAD_FAILURE, expectedErrorMsg));
        assertEquals(AppState.GifStatus.NOT_DOWNLOADED, currentState().getGifStatus());
        assertEquals("", currentState().getGifLocalPath());
        assertEquals(expectedErrorMsg, currentState().getGifDownloadFailureMsg());
    }

    @Test
    public void whenSendPlayAction() throws Exception {
        dispatchAction(new Action(GifActionCreator.GIF_PLAY));
        assertEquals(AppState.GifStatus.LOOPING, currentState().getGifStatus());
        assertEquals(true, currentState().getGifWatched());
    }

    @Test
    public void whenSendPauseAction() throws Exception {
        dispatchAction(new Action(GifActionCreator.GIF_PAUSE));
        assertEquals(AppState.GifStatus.PAUSED, currentState().getGifStatus());
    }

    // Helpers methods to dry up unit tests
    private AppState currentState(){
        return getApp().getFluxxan().getState();
    }

    private void dispatchAction(Action action){
        getApp().getFluxxan().getDispatcher().dispatch(action);
        // Since the dispatcher send actions in background we need wait the response arrives
        synchronized (stateChanged){
            while(!stateChanged){
                sleep(5);
                Log.v(TAG, "dispatching...");
            };
            stateChanged = false;
        }
    }

    private App getApp(){
        return (App)RuntimeEnvironment.application;
    }

    private void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(TAG, "", e);
        }
    }
}