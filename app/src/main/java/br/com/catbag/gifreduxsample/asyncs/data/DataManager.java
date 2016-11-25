package br.com.catbag.gifreduxsample.asyncs.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snappydb.SnappydbException;
import com.umaplay.fluxxan.util.ThreadUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.retrofit.RetrofitBuilder;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.api.RiffsyRoutes;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyMedia;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyResponse;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyResult;
import br.com.catbag.gifreduxsample.asyncs.data.storage.Database;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by felipe on 26/10/16.
 */

public class DataManager {
    private static Float sRiffsyNext = null;
    private final RiffsyRoutes mRiffsyRoutes;

    private Database mDatabase;
    private boolean mIsSavingAppState = false;
    private SaveAppStateRunnable mNextSaveAppStateRunnable;

    public DataManager(Context context) {
        mDatabase = new Database(context);
        mRiffsyRoutes = RetrofitBuilder.getInstance()
                .createApiEndpoint(RiffsyRoutes.class, RiffsyRoutes.BASE_URL);
    }

    public boolean isSavingAppState() {
        return mIsSavingAppState;
    }

    public void saveAppState(AppState appState) {
        mNextSaveAppStateRunnable = new DataManager.SaveAppStateRunnable(appState);
        executeSaveAppStateTask();
    }

    public void loadAppState(final LoadAppStateListener listener) {
        new Thread(() -> {
            try {
                listener.onLoaded(mDatabase.getAppState());
            } catch (SnappydbException | IOException e) {
                listener.onLoaded(getAppStateDefault());
                Log.e(getClass().getSimpleName(), "appstate not loaded", e);
            }
        }).start();
    }

    public void fetchGifs(final GifListLoadListener listener) {
        Call call = mRiffsyRoutes.getTrendingResults(RiffsyRoutes.DEFAULT_LIMIT_COUNT, sRiffsyNext);
        call.enqueue(new Callback<RiffsyResponse>() {
            @Override
            public void onResponse(Call<RiffsyResponse> call, Response<RiffsyResponse> response) {
                Map<String, Gif> gifs = extractGifsFromRiffsyResponse(response);

                sRiffsyNext = response.body().next();
                boolean hasMore = sRiffsyNext != null;
                listener.onLoaded(gifs, hasMore);
            }

            @Override
            public void onFailure(Call<RiffsyResponse> call, Throwable t) {
                Log.e(getClass().getSimpleName(), "onFailure", t);
            }
        });
    }

    private void executeSaveAppStateTask() {
        if (mNextSaveAppStateRunnable != null && !mIsSavingAppState) {
            mIsSavingAppState = true;
            ThreadUtils.runInBackground(mNextSaveAppStateRunnable);
            mNextSaveAppStateRunnable = null;
        }
    }

    private void onFinishedSaveAppStateTask() {
        ThreadUtils.runOnMain(() -> {
            mIsSavingAppState = false;
            executeSaveAppStateTask();
        });
    }

    @NonNull
    private Map<String, Gif> extractGifsFromRiffsyResponse(Response<RiffsyResponse> response) {
        List<RiffsyResult> results = response.body().results();
        Map<String, Gif> gifs = new LinkedHashMap<>();
        for (RiffsyResult result : results) {
            List<RiffsyMedia> riffsyMedias = result.media();
            if (riffsyMedias.size() > 0) {
                Gif gif = ImmutableGif.builder()
                        .url(riffsyMedias.get(0).gif().url())
                        .title(result.title())
                        .uuid(UUID.randomUUID().toString()).build();
                gifs.put(gif.getUuid(), gif);
            }
        }
        return gifs;
    }

    public static AppState getAppStateDefault() {
        String[] uuids = {"1", "2", "3", "4", "5" };
        String[] titles = {"Gif 1", "Gif 2", "Gif 3", "Gif 4", "Gif 5" };
        String[] urls = {
                "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif",
                "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/"
                        + "b3368a682fc5ff891e41baad2731f4b6.gif",
                "https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif",
                "https://media.giphy.com/media/l2YWl1oQlNvthGWrK/giphy.gif",
                "https://media.giphy.com/media/3oriNQHSU0bVcFW5sA/giphy.gif"
        };

        Map<String, Gif> gifs = new LinkedHashMap<>();
        for (int i = 0; i < titles.length; i++) {
            Gif gif = buildGif(uuids[i], titles[i], urls[i]);
            gifs.put(gif.getUuid(), gif);
        }

        return ImmutableAppState.builder().putAllGifs(gifs).build();
    }

    private static Gif buildGif(String uuid, String title, String url) {
        return ImmutableGif.builder().uuid(uuid).title(title).url(url).build();
    }

    public interface LoadAppStateListener {
        void onLoaded(AppState appState);
    }

    public interface GifListLoadListener {
        void onLoaded(Map<String, Gif> gifs, boolean hasMore);
    }

    private class SaveAppStateRunnable implements Runnable {

        private AppState mAppState;

        public SaveAppStateRunnable(AppState appState) {
            mAppState = appState;
        }

        @Override
        public void run() {
            try {
                mDatabase.saveAppState(mAppState);
            } catch (SnappydbException | JsonProcessingException e) {
                Log.e(getClass().getSimpleName(), "unsaved appstate", e);
            }
            onFinishedSaveAppStateTask();
        }

    }

}
