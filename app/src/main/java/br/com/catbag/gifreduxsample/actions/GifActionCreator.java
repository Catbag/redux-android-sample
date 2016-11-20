package br.com.catbag.gifreduxsample.actions;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import br.com.catbag.gifreduxsample.MyApp;
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

    private GifActionCreator() {
        MyApp.getFluxxan().inject(this);
    }

    public static GifActionCreator getInstance() {
        if (sInstance == null) {
            sInstance = new GifActionCreator();
        }
        return sInstance;
    }

    public void gifDownloadStart(Gif gif) {
        dispatch(new Action(GIF_DOWNLOAD_START, gif.getUuid()));
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
