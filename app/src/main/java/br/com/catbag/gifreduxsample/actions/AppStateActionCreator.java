package br.com.catbag.gifreduxsample.actions;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import br.com.catbag.gifreduxsample.MyApp;

/**
 * Created by niltonvasques on 10/12/16.
 */

public final class AppStateActionCreator extends BaseActionCreator {

    public static final String APP_STATE_LOADED = "APP_STATE_LOADED";
    private static AppStateActionCreator sInstance;

    private AppStateActionCreator() {
        MyApp.getFluxxan().inject(this);
    }

    public static AppStateActionCreator getInstance() {
        if (sInstance == null) {
            sInstance = new AppStateActionCreator();
        }
        return sInstance;
    }

    public void loadAppState() {
        MyApp.getDataManager()
                .getAppState(appState -> dispatch(new Action(APP_STATE_LOADED, appState)));
    }

}