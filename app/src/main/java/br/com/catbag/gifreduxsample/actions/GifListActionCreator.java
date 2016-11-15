package br.com.catbag.gifreduxsample.actions;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import br.com.catbag.gifreduxsample.MyApp;

/**
 * Created by niltonvasques on 10/12/16.
 */

public final class GifListActionCreator extends BaseActionCreator {

    public static final String GIF_LIST_UPDATED = "GIF_LIST_UPDATED";
    public static final String GIF_LIST_FETCHING = "GIF_LIST_FETCHING";

    private static GifListActionCreator sInstance;

    private GifListActionCreator() {
        MyApp.getFluxxan().inject(this);
    }

    public static GifListActionCreator getInstance() {
        if (sInstance == null) {
            sInstance = new GifListActionCreator();
        }
        return sInstance;
    }

    public void loadGifs() {
        dispatch(new Action(GIF_LIST_FETCHING));
    }
}
