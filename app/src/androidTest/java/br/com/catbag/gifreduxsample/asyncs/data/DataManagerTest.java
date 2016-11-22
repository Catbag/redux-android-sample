package br.com.catbag.gifreduxsample.asyncs.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by felipe on 03/11/16.
 */

@RunWith(AndroidJUnit4.class)
public class DataManagerTest {

    private static final String TAG_APP_STATE = "TAG_APP_STATE";

    @Rule
    public Timeout globalTimeout = Timeout.seconds(30);

    private DataManager mDataManager;

    @Before
    public void setup() {
        mDataManager = new DataManager(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void cleanup() throws SnappydbException {
        DB db = DBFactory.open(InstrumentationRegistry.getTargetContext());
        db.destroy();
    }

    @Test
    public void whenAppStateSavedOnceTime() {
        AppState newAppState = buildAppState(1);
        mDataManager.saveAppState(newAppState);
        assertSaveAppState(newAppState);
    }

    @Test
    public void whenAppStateSavedSeveralTime() {
        AppState newAppState = null;
        for (int i = 0; i < 20; i++) {
            newAppState = buildAppState(i);
            mDataManager.saveAppState(newAppState);
        }
        assertSaveAppState(newAppState);
    }

    @Test
    public void whenSavedAppStateIsLoaded() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        AppState appState = buildAppState();
        saveAppState(appState);
        mDataManager.loadAppState(appStateLoaded -> {
            assertEquals(appState, appStateLoaded);
            signal.countDown();
        });
        signal.await();
    }

    @Test
    public void whenUnsavedAppStateIsLoaded() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        mDataManager.loadAppState(appStateLoaded -> {
            assertTrue(appStateLoaded != null);
            signal.countDown();
        });
        signal.await();
    }

    private void assertSaveAppState(AppState appState) {
        while (mDataManager.isSavingAppState()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Log.e(getClass().getSimpleName(), "sleep error", e);
            }
        }
        assertEquals(appState, getAppStateFromDB());
    }

    private AppState getAppStateFromDB() {
        AppState appstate = null;
        try {
            DB db = DBFactory.open(InstrumentationRegistry.getTargetContext());
            appstate = AppState.fromJson(db.get(TAG_APP_STATE));
            db.close();
        } catch (SnappydbException | IOException e) {
            Log.e(getClass().getSimpleName(), "error", e);
        }
        return appstate;
    }

    private void saveAppState(AppState appState) {
        try {
            DB db = DBFactory.open(InstrumentationRegistry.getTargetContext());
            db.put(TAG_APP_STATE, appState.toJson());
            db.close();
        } catch (SnappydbException | IOException e) {
            Log.e(getClass().getSimpleName(), "error", e);
        }
    }

    private AppState buildAppState() {
        return ImmutableAppState.builder().build();
    }

    private AppState buildAppState(int amountGifs) {
        return ImmutableAppState.builder().putAllGifs(buildGifs(amountGifs)).build();
    }

    private Map<String, Gif> buildGifs(int amount) {
        Map<String, Gif> gifs = new LinkedHashMap<>();
        for (int i = 0; i < amount; i++) {
            Gif gif = buildGif(i);
            gifs.put(gif.getUuid(), gif);
        }
        return gifs;
    }

    private Gif buildGif(int index) {
        return ImmutableGif.builder()
                .uuid("" + index)
                .title("Title " + index)
                .url("url" + index)
                .status(Gif.Status.values()[index % Gif.Status.values().length])
                .build();
    }

}