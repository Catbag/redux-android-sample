package br.com.catbag.gifreduxsample.reducers.reducers;

import com.umaplay.fluxxan.Action;

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
import shared.ReduxBaseTest;
import shared.TestHelper;

import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_FAILURE;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_START;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_SUCCESS;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_PAUSE;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_PLAY;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static shared.TestHelper.DEFAULT_UUID;
import static shared.TestHelper.buildAppState;
import static shared.TestHelper.buildFiveGifs;
import static shared.TestHelper.buildGif;
import static shared.TestHelper.gifBuilderWithDefault;

// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants=BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class GifListReducerTest extends ReduxBaseTest {

    public GifListReducerTest() {
        mHelper = new TestHelper(((MyApp) RuntimeEnvironment.application).getFluxxan());
    }

    @Test
    public void initialAppState() throws Exception {
        assertTrue(mHelper.getFluxxan().getState().getGifs().isEmpty());
        assertTrue(mHelper.getFluxxan().getState().getHasMoreGifs());
    }

    @Test
    public void whenSendDownloadStartedAction() throws Exception {
        assertTransition(Gif.Status.NOT_DOWNLOADED, Gif.Status.DOWNLOADING, GIF_DOWNLOAD_START,
                DEFAULT_UUID);
    }

    @Test
    public void whenSendLoadListAction() throws Exception {
        List<Gif> fakeGifs = buildFiveGifs();

        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_GIFS, fakeGifs);
        params.put(PayloadParams.PARAM_HAS_MORE, false);
        mHelper.dispatchAction(new Action(GifListActionCreator.GIF_LIST_UPDATED, params));

        Map<String, Gif> gifsStates = mHelper.getFluxxan().getState().getGifs();
        assertEquals(fakeGifs.size(), gifsStates.size());
        assertFalse(mHelper.getFluxxan().getState().getHasMoreGifs());

        for (Gif expectedGif : fakeGifs) {
            assertEquals(expectedGif, gifsStates.get(expectedGif.getUuid()));
        }
    }

    @Test
    public void whenSendLoadListWithANonEmptyListAction() throws Exception {
        List<Gif> fakeGifs = buildFiveGifs();

        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_GIFS, fakeGifs);
        params.put(PayloadParams.PARAM_HAS_MORE, true);
        mHelper.dispatchAction(new Action(GifListActionCreator.GIF_LIST_UPDATED, params));

        List<Gif> fakeGifs2 = buildFiveGifs();
        params = new HashMap<>();
        params.put(PayloadParams.PARAM_GIFS, fakeGifs2);
        params.put(PayloadParams.PARAM_HAS_MORE, false);
        mHelper.dispatchAction(new Action(GifListActionCreator.GIF_LIST_UPDATED, params));

        Map<String, Gif> gifsStates = mHelper.getFluxxan().getState().getGifs();
        assertEquals(fakeGifs.size() + fakeGifs2.size(), gifsStates.size());
        assertFalse(mHelper.getFluxxan().getState().getHasMoreGifs());

        for (Gif expectedGif : fakeGifs) {
            assertEquals(expectedGif, gifsStates.get(expectedGif.getUuid()));
        }
        for (Gif expectedGif : fakeGifs2) {
            assertEquals(expectedGif, gifsStates.get(expectedGif.getUuid()));
        }
    }

    @Test
    public void whenSendDownloadSuccessAction() throws Exception {
        String expectedPath = "/test/image.gif";
        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_PATH, expectedPath);
        params.put(PayloadParams.PARAM_UUID, DEFAULT_UUID);

        assertTransition(Gif.Status.DOWNLOADING, Gif.Status.DOWNLOADED, GIF_DOWNLOAD_SUCCESS,
                params);
        assertEquals(expectedPath, mHelper.getFluxxan().getState().getGifs().get(DEFAULT_UUID).getPath());
    }

    @Test
    public void whenSendDownloadFailureAction() throws Exception {
        assertTransition(Gif.Status.DOWNLOADING, Gif.Status.DOWNLOAD_FAILED, GIF_DOWNLOAD_FAILURE,
                DEFAULT_UUID);
    }

    @Test
    public void whenSendPlayAction() throws Exception {
        HashSet fromSet = new HashSet();
        fromSet.add(Gif.Status.DOWNLOADED);
        fromSet.add(Gif.Status.PAUSED);
        assertTransition(fromSet, Gif.Status.LOOPING, GIF_PLAY, DEFAULT_UUID);
        assertEquals(true, mHelper.firstAppStateGif().getWatched());
    }

    @Test
    public void whenSendPauseAction()  {
        assertTransition(Gif.Status.LOOPING, Gif.Status.PAUSED, GIF_PAUSE, DEFAULT_UUID);
    }

    @Test
    public void whenSendManyActions() {
        List gifs = new ArrayList();
        gifs.add(buildGif(Gif.Status.NOT_DOWNLOADED, "1"));
        gifs.add(buildGif(Gif.Status.DOWNLOADING, "2"));
        gifs.add(buildGif(Gif.Status.LOOPING, "3"));

        mHelper.dispatchAction(new Action(FakeReducer.FAKE_REDUCE_ACTION,
                buildAppState(new ArrayList<>())));

        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_GIFS, gifs);
        params.put(PayloadParams.PARAM_HAS_MORE, false);
        mHelper.dispatchAction(new Action(GifListActionCreator.GIF_LIST_UPDATED, params));
        mHelper.dispatchAction(new Action(GIF_DOWNLOAD_START, "1"));

        params = new HashMap<>();
        params.put(PayloadParams.PARAM_PATH, "");
        params.put(PayloadParams.PARAM_UUID, "2");
        mHelper.dispatchAction(new Action(GIF_DOWNLOAD_SUCCESS, params));
        mHelper.dispatchAction(new Action(GIF_PAUSE, "3"));

        Map expected = new HashMap();
        expected.put("1", buildGif(Gif.Status.DOWNLOADING, "1"));
        expected.put("2", buildGif(Gif.Status.DOWNLOADED, "2"));
        expected.put("3", buildGif(Gif.Status.PAUSED, "3"));
        AppState expectedAppState = ImmutableAppState.builder()
                .gifs(expected)
                .hasMoreGifs(false)
                .build();

        assertEquals(expectedAppState, mHelper.getFluxxan().getState());
    }

    @Test
    public void whenCompleteAppFlowActions() {
        mHelper.dispatchAction(new Action(FakeReducer.FAKE_REDUCE_ACTION,
                buildAppState(new ArrayList<>())));

        List gifs = new ArrayList();
        gifs.add(buildGif(Gif.Status.NOT_DOWNLOADED));

        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_GIFS, gifs);
        params.put(PayloadParams.PARAM_HAS_MORE, false);
        mHelper.dispatchAction(new Action(GifListActionCreator.GIF_LIST_UPDATED, params));
        mHelper.dispatchAction(new Action(GIF_DOWNLOAD_START, DEFAULT_UUID));

        params = new HashMap<>();
        params.put(PayloadParams.PARAM_PATH, "");
        params.put(PayloadParams.PARAM_UUID, DEFAULT_UUID);
        mHelper.dispatchAction(new Action(GIF_DOWNLOAD_SUCCESS, params));
        mHelper.dispatchAction(new Action(GIF_PLAY, DEFAULT_UUID));
        mHelper.dispatchAction(new Action(GIF_PAUSE, DEFAULT_UUID));

        Map expected = new HashMap();
        Gif gif = gifBuilderWithDefault()
                .watched(true)
                .status(Gif.Status.PAUSED)
                .build();

        expected.put(DEFAULT_UUID, gif);
        AppState expectedAppState = ImmutableAppState.builder()
                .gifs(expected)
                .hasMoreGifs(false)
                .build();

        assertEquals(expectedAppState, mHelper.getFluxxan().getState());
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
            mHelper.dispatchFakeAppState(buildAppState(buildGif(status, uuid)));
            mHelper.dispatchAction(new Action(action, payload));
            assertNotEquals(to, mHelper.getFluxxan().getState().getGifs().get(uuid).getStatus());
        }

        for (Iterator<Gif.Status> iterator = from.iterator(); iterator.hasNext(); ) {
            Gif.Status fromStatus = iterator.next();

            mHelper.dispatchFakeAppState(buildAppState(buildGif(fromStatus, uuid)));
            mHelper.dispatchAction(new Action(action, payload));
            assertEquals(to, mHelper.getFluxxan().getState().getGifs().get(uuid).getStatus());
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
