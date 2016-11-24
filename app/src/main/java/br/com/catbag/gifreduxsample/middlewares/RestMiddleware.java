package br.com.catbag.gifreduxsample.middlewares;

import android.content.Context;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseMiddleware;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.asyncs.net.downloader.FileDownloader;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;

import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_FAILURE;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_START;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_SUCCESS;
import static br.com.catbag.gifreduxsample.actions.GifListActionCreator.GIF_LIST_FETCHING;
import static br.com.catbag.gifreduxsample.actions.GifListActionCreator.GIF_LIST_UPDATED;

/**
 * Created by niltonvasques on 11/13/16.
 */

public class RestMiddleware extends BaseMiddleware<AppState> {
    private final Context mContext;
    private final FileDownloader mFileDownloader;
    private DataManager mDataManager;

    public RestMiddleware(Context context, DataManager dataManager, FileDownloader fileDownloader) {
        super();
        mContext = context;
        mDataManager = dataManager;
        mFileDownloader = fileDownloader;
    }

    @Override
    public void intercept(AppState appState, Action action) throws Exception {
        switch(action.Type) {
            case GIF_DOWNLOAD_START:
                downloadGif(appState, action);
                break;
            case GIF_LIST_FETCHING:
                fetchGifs(appState);
                break;
            default:
                break;
        }
    }

    private void fetchGifs(AppState appState) {
        if (appState.getHasMoreGifs()) {
            mDataManager.fetchGifs((gifs, hasMore) -> {
                Map<String, Object> params = new HashMap<>();
                params.put(PayloadParams.PARAM_GIFS, gifs);
                params.put(PayloadParams.PARAM_HAS_MORE, hasMore);
                mDispatcher.dispatch(new Action(GIF_LIST_UPDATED, params));
            });
        }
    }

    private void downloadGif(AppState appState, Action action) {
        Gif gif = appState.getGifs().get(action.Payload.toString());
        String pathToSave = mContext.getExternalFilesDir(null) + File.separator
                + gif.getUuid() + ".gif";

        mFileDownloader.download(gif.getUrl(), pathToSave,
                () -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put(PayloadParams.PARAM_UUID, gif.getUuid());
                    params.put(PayloadParams.PARAM_PATH, pathToSave);
                    mDispatcher.dispatch(new Action(GIF_DOWNLOAD_SUCCESS, params));
                },
                e -> mDispatcher.dispatch(new Action(GIF_DOWNLOAD_FAILURE, gif.getUuid())));
    }
}
