package br.com.catbag.giffluxsample.actions;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.ActionCreator;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import br.com.catbag.giffluxsample.App;
import br.com.catbag.giffluxsample.models.AppState;

/**
 * Created by niltonvasques on 10/12/16.
 */

public class GifActionCreator extends BaseActionCreator {
    public static final String APP_START = "APP_START";
    public static final String PLAY_GIF = "PLAY_GIF";
    public static final String PAUSE_GIF = "PAUSE_GIF";

    private static GifActionCreator instance;

    private GifActionCreator() {
        App.getFluxxan().inject(this);
    }

    public static GifActionCreator getInstance() {
        if(instance == null) {
            instance = new GifActionCreator();
        }

        return instance;
    }

    public void start() {
        dispatch(new Action<>(APP_START));
    }

    public void play() {
        dispatch(new Action<>(PLAY_GIF));
    }

    public void pause() {
        dispatch(new Action<>(PAUSE_GIF));
    }

    public void gifClick(AppState.GifStatus status) {
        if (status == AppState.GifStatus.LOOPING) {
            pause();
        }
        else {
            play();
        }
    }
}
