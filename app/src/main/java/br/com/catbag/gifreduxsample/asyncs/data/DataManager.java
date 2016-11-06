package br.com.catbag.gifreduxsample.asyncs.data;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snappydb.SnappydbException;
import com.umaplay.fluxxan.StateListener;

import java.io.IOException;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.asyncs.data.storage.Database;
import br.com.catbag.gifreduxsample.asyncs.net.rest.retrofit.RetrofitBuilder;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.api.RiffsyRoutes;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyMedia;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResponse;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResult;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by felipe on 26/10/16.
 */

public class DataManager implements StateListener<AppState> {
    private static Float sRiffsyNext = null;
    private final RiffsyRoutes mRiffsyRoutes;

    private Database mDatabase;
    private boolean mIsSavingAppState = false;
    private SaveAppStateTask mNextSaveAppStateTask;

    public DataManager(Context context) {
        mDatabase = new Database(context);
        mRiffsyRoutes = RetrofitBuilder.getInstance()
                .createApiEndpoint(RiffsyRoutes.class, RiffsyRoutes.BASE_URL);
    }

    public void start() {
        MyApp.getFluxxan().addListener(this);
    }

    public void stop() {
        MyApp.getFluxxan().removeListener(this);
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        return newState != oldState;
    }

    @Override
    public void onStateChanged(AppState appState) {
        mNextSaveAppStateTask = new SaveAppStateTask(appState);
        executeSaveAppStateTask();
    }

    public boolean isSavingAppState() {
        return mIsSavingAppState;
    }

    public void getAppState(final LoadAppStateListener listener) {
        new Thread(() -> {
            try {
                listener.onLoaded(mDatabase.getAppState());
            } catch (SnappydbException | IOException e) {
                listener.onLoaded(mDatabase.seed());
                Log.e(getClass().getSimpleName(), "appstate not loaded", e);
            }
        }).start();
    }

    public void fetchGifs(final GifListLoadListener listener) {
        Call call = mRiffsyRoutes.getTrendingResults(RiffsyRoutes.DEFAULT_LIMIT_COUNT, sRiffsyNext);
        call.enqueue(new Callback<RiffsyResponse>() {
            @Override
            public void onResponse(Call<RiffsyResponse> call, Response<RiffsyResponse> response) {
                List<Gif> gifs = extractGifsFromRiffsyResponse(response);

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
        if (mNextSaveAppStateTask != null && !mIsSavingAppState) {
            mIsSavingAppState = true;
            mNextSaveAppStateTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            mNextSaveAppStateTask = null;
        }
    }

    private void onFinishedSaveAppStateTask() {
        mIsSavingAppState = false;
        executeSaveAppStateTask();
    }

    @NonNull
    private List<Gif> extractGifsFromRiffsyResponse(Response<RiffsyResponse> response) {
        List<RiffsyResult> results = response.body().results();
        List<Gif> gifs = new ArrayList<Gif>();
        for (RiffsyResult result : results) {
            List<RiffsyMedia> riffsyMedias = result.media();
            if (riffsyMedias.size() > 0) {
                Gif gif = ImmutableGif.builder()
                        .url(riffsyMedias.get(0).gif().url())
                        .title(result.title())
                        .uuid(UUID.randomUUID().toString()).build();
                gifs.add(gif);
            }
        }
        return gifs;
    }

    public interface LoadAppStateListener {
        void onLoaded(AppState appState);
    }

    public interface GifListLoadListener {
        void onLoaded(List<Gif> gifs, boolean hasMore);
    }

    private class SaveAppStateTask extends AsyncTask<Void, Void, Boolean> {
        private AppState mAppState;

        public SaveAppStateTask(AppState appState) {
            mAppState = appState;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                mDatabase.saveAppState(mAppState);
            } catch (SnappydbException | JsonProcessingException e) {
                Log.e(getClass().getSimpleName(), "unsaved appstate", e);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            onFinishedSaveAppStateTask();
        }
    }

}