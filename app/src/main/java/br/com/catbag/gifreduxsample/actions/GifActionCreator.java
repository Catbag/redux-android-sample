package br.com.catbag.gifreduxsample.actions;

import android.content.Context;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.asyncs.downloader.FileDownloader;
import br.com.catbag.gifreduxsample.models.Gif;

/**
 * Created by niltonvasques on 10/12/16.
 */

public final class GifActionCreator extends BaseActionCreator {
    public static final String GIF_PLAY = "GIF_PLAY";
    public static final String GIF_PAUSE = "GIF_PAUSE";
    public static final String GIF_DOWNLOAD_SUCCESS = "GIF_DOWNLOAD_SUCCESS";
    public static final String GIF_DOWNLOAD_FAILURE = "GIF_DOWNLOAD_FAILURE";
    public static final String GIF_DOWNLOAD_START = "GIF_DOWNLOAD_START";

    private static GifActionCreator sInstance;

    private FileDownloader mFileDownloader;

    private GifActionCreator() {
        MyApp.getFluxxan().inject(this);
        mFileDownloader = new FileDownloader();
    }

    public static GifActionCreator getInstance() {
        if (sInstance == null) {
            sInstance = new GifActionCreator();
        }
        return sInstance;
    }

    public void gifDownloadStart(Gif state, Context context) {
        dispatch(new Action(GIF_DOWNLOAD_START, state.getUuid()));
        String pathToSave = context.getExternalFilesDir(null) + File.separator
                + state.getUuid() + ".gif";

        // with just one filedownloader the callbacks are replaced all time
        mFileDownloader.download(state.getUrl(), pathToSave,
                () -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put(PayloadParams.PARAM_UUID, state.getUuid());
                    params.put(PayloadParams.PARAM_PATH, pathToSave);
                    dispatch(new Action(GIF_DOWNLOAD_SUCCESS, params));
                },
                e -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put(PayloadParams.PARAM_UUID, state.getUuid());
                    params.put(PayloadParams.PARAM_DOWNLOAD_FAILURE_MSG, e.getMessage());
                    dispatch(new Action(GIF_DOWNLOAD_FAILURE, params));
                });

    }

    public void gifClick(Gif state) {
        if (state.getStatus() == Gif.Status.DOWNLOADED || state.getStatus() == Gif
                .Status.PAUSED) {
            gifPlay(state.getUuid());
        } else if (state.getStatus() == Gif.Status.LOOPING) {
            gifPause(state.getUuid());
        }
    }

    private void gifPlay(String uuid) {
        dispatch(new Action(GIF_PLAY, uuid));
    }

    private void gifPause(String uuid) {
        dispatch(new Action(GIF_PAUSE, uuid));
    }
}
