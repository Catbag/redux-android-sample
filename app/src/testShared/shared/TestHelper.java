package shared;

import android.util.Log;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.StateListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import br.com.catbag.gifreduxsample.helpers.AppStateHelper;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

/**
 * Created by niltonvasques on 10/31/16.
 */

public class TestHelper {
    public static final String STRING_UNIQUE = UUID.randomUUID().toString();
    public static final String STRING_UNIQUE_2 = UUID.randomUUID().toString();
    public static final String STRING_UNIQUE_3 = UUID.randomUUID().toString();
    public static final String DEFAULT_UUID = "default-uuid";
    public static final Float FLOAT_RANDOM = new Random().nextFloat();
    public static final float DELTA = 0.00001f;

    private final Object mLock = new Object();

    private Fluxxan mFluxxan;
    private boolean mStateChanged = false;

    private StateListener mStateListener = new StateListener() {
        @Override
        public boolean hasStateChanged(Object newState, Object oldState) {
            setStateChanged(true);
            return newState != oldState;
        }

        @Override
        public void onStateChanged(Object o) {
        }
    };

    public TestHelper(Fluxxan fluxxan) {
        mFluxxan = fluxxan;
    }

    public void activateStateListener() {
        getFluxxan().getDispatcher().addListener(mStateListener);
    }

    public void deactivateStateListener() {
        getFluxxan().getDispatcher().removeListener(mStateListener);
    }

    private boolean isStateChanged() {
        synchronized (mLock) {
            return mStateChanged;
        }
    }

    private void setStateChanged(boolean value) {
        synchronized (mLock) {
            mStateChanged = value;
        }
    }

    public Fluxxan<AppState> getFluxxan() {
        return mFluxxan;
    }

    // Helpers methods to dry up unit tests
    public void dispatchAction(Action action) {
        getFluxxan().getDispatcher().dispatch(action);
        // Since the dispatcher send actions in background we need wait the response arrives
        while(!isStateChanged() || getFluxxan().getDispatcher().isDispatching()) {
            sleep(5);
        }
        setStateChanged(false);
    }

    public void dispatchFakeAppState(AppState state) {
        dispatchAction(new Action(FakeReducer.FAKE_REDUCE_ACTION, state));
    }

    public Gif firstAppStateGif() {
        AppState state = getFluxxan().getState();
        if (state.getGifs().size() <= 0) return null;
        return (Gif) state.getGifs().values().toArray()[0];
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }

    public static AppState buildAppState(Gif gif) {
        return ImmutableAppState.builder().putGifs(gif.getUuid(), gif).build();
    }

    public static AppState buildAppState(List<Gif> gifs) {
        return ImmutableAppState.builder().putAllGifs(AppStateHelper.gifListToMap(gifs)).build();
    }

    public static List<Gif> buildFiveGifs() {
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

    public static Gif buildGif() {
        return builderWithDefault().build();
    }

    public static Gif buildGif(Gif.Status status) {
        return buildGif(status, DEFAULT_UUID);
    }

    public static Gif buildGif(Gif.Status status, String uuid) {
        return builderWithDefault()
                .uuid(uuid)
                .status(status)
                .build();
    }

    public static ImmutableGif.Builder builderWithEmpty() {
        return ImmutableGif.builder()
                .uuid(UUID.randomUUID().toString())
                .title("")
                .url("");
    }

    public static ImmutableGif.Builder builderWithDefault() {
        return ImmutableGif.builder()
                .uuid(DEFAULT_UUID)
                .title("Gif")
                .url( "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif")
                .status(Gif.Status.NOT_DOWNLOADED);

    }
}
