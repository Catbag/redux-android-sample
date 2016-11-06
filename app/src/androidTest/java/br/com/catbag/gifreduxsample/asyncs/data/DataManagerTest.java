package br.com.catbag.gifreduxsample.asyncs.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

import static junit.framework.Assert.assertEquals;

/**
 * Created by felipe on 03/11/16.
 */

@RunWith(AndroidJUnit4.class)
public class DataManagerTest {

    private static final String TAG_APP_STATE = "TAG_APP_STATE";
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

    @Test(timeout = 1000)
    public void whenAppStateChangesOnceTime() throws SnappydbException {
        AppState oldAppState = createAppState();
        AppState newAppState = createAppStateFrom(oldAppState, 1);
        mDataManager.onStateChanged(newAppState);
        assertSaveAppState(newAppState);
    }

    @Test(timeout = 1000)
    public void whenAppStateChangesSeveralTime() throws SnappydbException {
        AppState oldAppState = createAppState();
        AppState newAppState = null;
        for (int i=0; i<100; i++) {
            newAppState = createAppStateFrom(oldAppState, i);
            mDataManager.onStateChanged(newAppState);
            oldAppState = newAppState;
        }
        assertSaveAppState(newAppState);
    }

    private void assertSaveAppState(AppState appState) {
        while(mDataManager.isSavingAppState()) {
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

    private AppState createAppState() {
        return ImmutableAppState.builder().build();
    }

    private AppState createAppStateFrom(AppState fromState, int amountGifs) {
        return ImmutableAppState.builder()
                .from(fromState)
                .putAllGifs(createGifsMap(amountGifs))
                .build();
    }

    private Map<String, Gif> createGifsMap(int amountGifs) {
        Map<String, Gif> gifsMap = new HashMap<>();
        for (int i=0; i<amountGifs; i++) {
            Gif gif = createGif(i);
            gifsMap.put(gif.getUuid(), gif);
        }
        return gifsMap;
    }

    private Gif createGif(int index) {
        return ImmutableGif.builder().uuid(UUID.randomUUID().toString())
                .title("Title " + index)
                .url("url" + index)
                .status(Gif.Status.values()[index%Gif.Status.values().length])
                .build();
    }

}