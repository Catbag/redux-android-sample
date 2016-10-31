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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.catbag.gifreduxsample.BuildConfig;
import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_SUCCESS;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants=BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class GifListReducerTest {
    private Boolean mStateChanged = false;

    @Before
    public void setup(){
        getApp().getFluxxan().getDispatcher().addListener(new StateListener() {
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
        dispatchSomeGifs();
        dispatchAction(new Action(GifActionCreator.GIF_DOWNLOAD_START, getFirstGif().getUuid()));
        assertEquals(Gif.Status.DOWNLOADING, getFirstGif().getStatus());
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
        dispatchSomeGifs();
        String expectedPath = "/test/image.gif";
        Map<String, Object> params = new HashMap<>();
        params.put(PayloadParams.PARAM_UUID, getFirstGif().getUuid());
        params.put(PayloadParams.PARAM_PATH, expectedPath);
        dispatchAction(new Action(GIF_DOWNLOAD_SUCCESS, params));
        assertEquals(expectedPath, getFirstGif().getPath());
        assertEquals(Gif.Status.DOWNLOADED, getFirstGif().getStatus());
    }

    @Test(timeout = 1000)
    public void whenSendDownloadFailureAction() throws Exception {
        dispatchSomeGifs();
        dispatchAction(new Action(GifActionCreator.GIF_DOWNLOAD_FAILURE, getFirstGif().getUuid()));
        assertEquals(Gif.Status.DOWNLOAD_FAILED, getFirstGif().getStatus());
    }

    @Test(timeout = 1000)
    public void whenSendPlayAction() throws Exception {
        dispatchSomeGifs();
        dispatchAction(new Action(GifActionCreator.GIF_PLAY, getFirstGif().getUuid()));
        assertEquals(Gif.Status.LOOPING, getFirstGif().getStatus());
        assertEquals(true, getFirstGif().getWatched());
    }

    @Test(timeout = 1000)
    public void whenSendPauseAction() throws Exception {
        dispatchSomeGifs();
        dispatchAction(new Action(GifActionCreator.GIF_PAUSE, getFirstGif().getUuid()));
        assertEquals(Gif.Status.PAUSED, getFirstGif().getStatus());
    }

    // Helpers methods to dry up unit tests
    private void dispatchAction(Action action){
        getApp().getFluxxan().getDispatcher().dispatch(action);
        // Since the dispatcher send actions in background we need wait the response arrives
        synchronized (mStateChanged){
            while(!mStateChanged){
                sleep(5);
            };
            mStateChanged = false;
        }
    }

    private MyApp getApp(){
        return (MyApp) RuntimeEnvironment.application;
    }

    private Gif getFirstGif() {
        AppState state = getApp().getFluxxan().getState();
       if (state.getGifs().size() <= 0) return null;
       return (Gif) state.getGifs().values().toArray()[0];
    }

    private void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(GifListReducerTest.class.getSimpleName(), "", e);
        }
    }

    private void dispatchSomeGifs() {
        dispatchAction(new Action(GifListActionCreator.GIF_LIST_LOADED, getFakeGifs()));
    }

    private List<Gif> getFakeGifs() {
        String[] titles = {"Gif 1", "Gif 2", "Gif 3", "Gif 4", "Gif 5" };
        String[] urls = {
                "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif",
                "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/"
                        + "b3368a682fc5ff891e41baad2731f4b6.gif",
                "https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif",
                "https://media.giphy.com/media/l2YWl1oQlNvthGWrK/giphy.gif",
                "https://media.giphy.com/media/3oriNQHSU0bVcFW5sA/giphy.gif"
        };

        List<Gif> gifs = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Gif gif = ImmutableGif.builder().uuid(UUID.randomUUID().toString())
                    .title(titles[i])
                    .url(urls[i])
                    .build();
            gifs.add(gif);
        }

        return gifs;
    }
}
