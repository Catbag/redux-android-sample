package br.com.catbag.gifreduxsample.actions;

import android.content.Context;
import android.util.Log;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import java.io.File;

import br.com.catbag.gifreduxsample.App;
import br.com.catbag.gifreduxsample.asyncs.restservice.FileDownloader;
import br.com.catbag.gifreduxsample.models.AppState;

/**
 * Created by niltonvasques on 10/12/16.
 */

public final class GifActionCreator extends BaseActionCreator {
    public static final String APP_START = "APP_START";
    public static final String GIF_PLAY = "GIF_PLAY";
    public static final String GIF_PAUSE = "GIF_PAUSE";
    public static final String GIF_DOWNLOAD_SUCCESS = "GIF_DOWNLOAD_SUCCESS";
    public static final String GIF_DOWNLOAD_FAILURE = "GIF_DOWNLOAD_FAILURE";
    public static final String GIF_DOWNLOAD_STARTED = "GIF_DOWNLOAD_STARTED";

    private static final String TAG = "GifActionCreator";
    private static GifActionCreator sInstance;

    private FileDownloader mFileDownloader;

    private GifActionCreator() {
        App.getFluxxan().inject(this);
        mFileDownloader = new FileDownloader();
    }

    public static GifActionCreator getInstance() {
        if (sInstance == null) {
            sInstance = new GifActionCreator();
        }
        return sInstance;
    }

    public void gifDownloadStart(String gifUrl, String gifTitle, Context context) {
        dispatch(new Action(APP_START));

        String pathToSave = context.getExternalFilesDir(null) + File.separator + gifTitle + ".gif";

        mFileDownloader.onStarted(() -> dispatch(new Action(GIF_DOWNLOAD_STARTED)))
                .onSuccess(() -> dispatch(new Action(GIF_DOWNLOAD_SUCCESS, pathToSave)))
                .onFailure(e -> {
                    Log.e(TAG, "", e);
                    dispatch(new Action(GIF_DOWNLOAD_FAILURE, e.getMessage()));
                })
                .download(gifUrl, pathToSave);
    }

    public void gifClick(AppState.GifStatus status) {
        if (status == AppState.GifStatus.DOWNLOADED || status == AppState.GifStatus.PAUSED) {
            gifPlay();
        } else if (status == AppState.GifStatus.LOOPING) {
            gifPause();
        }
    }

    public void setmFileDownloader(FileDownloader mFileDownloader) {
        this.mFileDownloader = mFileDownloader;
    }

    public FileDownloader getmFileDownloader() {
        return mFileDownloader;
    }

    private void gifPlay() {
        dispatch(new Action(GIF_PLAY));
    }

    private void gifPause() {
        dispatch(new Action(GIF_PAUSE));
    }
}
