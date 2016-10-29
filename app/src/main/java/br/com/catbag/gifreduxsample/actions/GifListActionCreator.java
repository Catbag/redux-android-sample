package br.com.catbag.gifreduxsample.actions;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;

/**
 * Created by niltonvasques on 10/12/16.
 */

public final class GifListActionCreator extends BaseActionCreator {

    public static final String LOAD_GIF_LIST = "LOAD_GIF_LIST";
    public static final String GIF_LIST_LOADED = "GIF_LIST_LOADED";

    private static GifListActionCreator sInstance;

    private DataManager mDataManager = new DataManager();

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
        dispatch(new Action(LOAD_GIF_LIST));
        mDataManager.getAllGifs(gifs -> dispatch(new Action(GIF_LIST_LOADED, gifs)));
    }

    public void setDataManager(DataManager dataManager) {
        mDataManager = dataManager;
    }

}
