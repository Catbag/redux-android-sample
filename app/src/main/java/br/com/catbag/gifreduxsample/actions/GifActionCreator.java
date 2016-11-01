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

    public void gifDownloadStart(Gif gif, Context context) {
        dispatch(new Action(GIF_DOWNLOAD_START, gif.getUuid()));
        String pathToSave = context.getExternalFilesDir(null) + File.separator
                + gif.getUuid() + ".gif";

        mFileDownloader.download(gif.getUrl(), pathToSave,
                () -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put(PayloadParams.PARAM_UUID, gif.getUuid());
                    params.put(PayloadParams.PARAM_PATH, pathToSave);
                    dispatch(new Action(GIF_DOWNLOAD_SUCCESS, params));
                },
                e -> {
                    dispatch(new Action(GIF_DOWNLOAD_FAILURE, gif.getUuid()));
                });

    }

    public void gifClick(Gif gif) {
        if (gif.getStatus() == Gif.Status.DOWNLOADED || gif.getStatus() == Gif
                .Status.PAUSED) {
            gifPlay(gif.getUuid());
        } else if (gif.getStatus() == Gif.Status.LOOPING) {
            gifPause(gif.getUuid());
        }
    }

    private void gifPlay(String uuid) {
        dispatch(new Action(GIF_PLAY, uuid));
    }

    private void gifPause(String uuid) {
        dispatch(new Action(GIF_PAUSE, uuid));
    }
}
