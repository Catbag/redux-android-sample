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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.catbag.gifreduxsample.BuildConfig;
import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import shared.FakeReducer;

import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_FAILURE;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_START;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_SUCCESS;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_PAUSE;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_PLAY;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static shared.TestUtils.DEFAULT_UUID;
import static shared.TestUtils.buildGif;
import static shared.TestUtils.createDefaultImmutableGif;
import static shared.TestUtils.createStateFromGif;
import static shared.TestUtils.createStateFromGifs;
import static shared.TestUtils.getFakeGifs;
import static shared.TestUtils.getFirstGif;
import static shared.TestUtils.getFluxxan;

// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants=BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class GifListReducerTest {
    private Boolean mStateChanged = false;

    @Before
    public void setup(){
        getFluxxan().registerReducer(new FakeReducer());
        getFluxxan().getDispatcher().addListener(new StateListener() {
            @Override
            public boolean hasStateChanged(Object newState, Object oldState) {
                mStateChanged = true;
                return newState != oldState;
            }

            @Override
            public void onStateChanged(Object o) {
            }
        });
    }

    @Test(timeout = 1000)
    public void initialAppState() throws Exception {
        assertTrue(getApp().getFluxxan().getState().getGifs().isEmpty());
    }

    @Test(timeout = 1000)
    public void whenSendDownloadStartedAction() throws Exception {
        assertTransition(Gif.Status.NOT_DOWNLOADED, Gif.Status.DOWNLOADING, GIF_DOWNLOAD_START,
                DEFAULT_UUID);
    }

    @Test(timeout = 1000)
    public void whenSendLoadListAction() throws Exception {
        List<Gif> fakeGifs = getFakeGifs();

        dispatchAction(new Action(GifListActionCreator.GIF_LIST_LOADED, fakeGifs));

        Map<String, Gif> gifsStates = getApp().getFluxxan().getState().getGifs();
        assertEquals(fakeGifs.size(), gifsStates.size());

        for (Gif expectedGif : fakeGifs) {
            assertEquals(expectedGif, gifsStates.get(expectedGif.getUuid()));
        }
    }

    @Test(timeout = 1000)
    public void whenSendDownloadSuccessAction() throws Exception {
        String expectedPath = "/test/image.gif";
        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_PATH, expectedPath);
        params.put(PayloadParams.PARAM_UUID, DEFAULT_UUID);

        assertTransition(Gif.Status.DOWNLOADING, Gif.Status.DOWNLOADED, GIF_DOWNLOAD_SUCCESS,
                params);
        assertEquals(expectedPath, getFluxxan().getState().getGifs().get(DEFAULT_UUID).getPath());
    }

    @Test(timeout = 1000)
    public void whenSendDownloadFailureAction() throws Exception {
        assertTransition(Gif.Status.DOWNLOADING, Gif.Status.DOWNLOAD_FAILED, GIF_DOWNLOAD_FAILURE,
                DEFAULT_UUID);
    }

    @Test(timeout = 1000)
    public void whenSendPlayAction() throws Exception {
        HashSet fromSet = new HashSet();
        fromSet.add(Gif.Status.DOWNLOADED);
        fromSet.add(Gif.Status.PAUSED);
        assertTransition(fromSet, Gif.Status.LOOPING, GIF_PLAY, DEFAULT_UUID);
        assertEquals(true, getFirstGif().getWatched());
    }

    @Test(timeout = 1000)
    public void whenSendPauseAction()  {
        assertTransition(Gif.Status.LOOPING, Gif.Status.PAUSED, GIF_PAUSE, DEFAULT_UUID);
    }

    @Test(timeout = 1000)
    public void whenSendManyActions() {
        List gifs = new ArrayList();
        gifs.add(buildGif(Gif.Status.NOT_DOWNLOADED, "1"));
        gifs.add(buildGif(Gif.Status.DOWNLOADING, "2"));
        gifs.add(buildGif(Gif.Status.LOOPING, "3"));

        dispatchAction(new Action(FakeReducer.FAKE_REDUCE_ACTION, createStateFromGifs(new ArrayList<>())));

        dispatchAction(new Action(GifListActionCreator.GIF_LIST_LOADED, gifs));
        dispatchAction(new Action(GIF_DOWNLOAD_START, "1"));

        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_PATH, "");
        params.put(PayloadParams.PARAM_UUID, "2");
        dispatchAction(new Action(GIF_DOWNLOAD_SUCCESS, params));
        dispatchAction(new Action(GIF_PAUSE, "3"));

        Map expected = new HashMap();
        expected.put("1", buildGif(Gif.Status.DOWNLOADING, "1"));
        expected.put("2", buildGif(Gif.Status.DOWNLOADED, "2"));
        expected.put("3", buildGif(Gif.Status.PAUSED, "3"));
        AppState expectedAppState = ImmutableAppState.builder()
                .gifs(expected)
                .build();

        assertEquals(expectedAppState, getApp().getFluxxan().getState());
    }

    @Test(timeout = 1000)
    public void whenCompleteAppFlowActions() {
        dispatchAction(new Action(FakeReducer.FAKE_REDUCE_ACTION, createStateFromGifs(new ArrayList<>())));

        List gifs = new ArrayList();
        gifs.add(buildGif(Gif.Status.NOT_DOWNLOADED));

        dispatchAction(new Action(GifListActionCreator.GIF_LIST_LOADED, gifs));
        dispatchAction(new Action(GIF_DOWNLOAD_START, DEFAULT_UUID));

        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_PATH, "");
        params.put(PayloadParams.PARAM_UUID, DEFAULT_UUID);
        dispatchAction(new Action(GIF_DOWNLOAD_SUCCESS, params));
        dispatchAction(new Action(GIF_PLAY, DEFAULT_UUID));
        dispatchAction(new Action(GIF_PAUSE, DEFAULT_UUID));

        Map expected = new HashMap();
        Gif gif = createDefaultImmutableGif().watched(true).status(Gif.Status.PAUSED).build();
        expected.put(DEFAULT_UUID, gif);
        AppState expectedAppState = ImmutableAppState.builder()
                .gifs(expected)
                .build();

        assertEquals(expectedAppState, getApp().getFluxxan().getState());
    }

    // Helpers methods to dry up unit tests
    private void dispatchAction(Action action) {
        getApp().getFluxxan().getDispatcher().dispatch(action);
        // Since the dispatcher send actions in background we need wait the response arrives
        synchronized (mStateChanged){
            while(!mStateChanged || getApp().getFluxxan().getDispatcher().isDispatching()) {
                sleep(5);
            }
            mStateChanged = false;
        }
    }

    private MyApp getApp(){
        return (MyApp) RuntimeEnvironment.application;
    }

    private void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }

    private void dispatchSomeGifs() {
        dispatchAction(new Action(FakeReducer.FAKE_REDUCE_ACTION, createStateFromGifs(getFakeGifs())));
    }

    public void dispatchOneGif(Gif gif) {
        dispatchAction(new Action(FakeReducer.FAKE_REDUCE_ACTION, createStateFromGif(gif)));
    }

    private void assertTransition(Gif.Status from, Gif.Status to, String action, Object payload) {
        HashSet fromSet = new HashSet();
        fromSet.add(from);
        assertTransition(fromSet, to, action, payload);
    }

    /** @param payload should be equals to reducers payload usage **/
    private void assertTransition(HashSet<Gif.Status> from, Gif.Status to, String action, Object payload) {
        Gif.Status[] statuses = { Gif.Status.NOT_DOWNLOADED, Gif.Status.DOWNLOADING,
                Gif.Status.DOWNLOADED, Gif.Status.LOOPING, Gif.Status.PAUSED };

        String uuid = extractUuidFromPayload(payload);

        for (Gif.Status status : statuses) {
            if(from.contains(status) || status == to) continue;
            dispatchOneGif(buildGif(status, uuid));
            dispatchAction(new Action(action, payload));
            assertNotEquals(to, getFluxxan().getState().getGifs().get(uuid).getStatus());
        }

        for (Iterator<Gif.Status> iterator = from.iterator(); iterator.hasNext(); ) {
            Gif.Status fromStatus = iterator.next();

            dispatchOneGif(buildGif(fromStatus, uuid));
            dispatchAction(new Action(action, payload));
            assertEquals(to, getFluxxan().getState().getGifs().get(uuid).getStatus());
        }
    }

    private String extractUuidFromPayload(Object payload) {
        String uuid = DEFAULT_UUID;
        if (payload instanceof String) {
            uuid = (String)payload;
        } else if (payload instanceof Map){
            uuid = (String) ((Map)payload).get(PayloadParams.PARAM_UUID);
        }
        return uuid;
    }

}
