package br.com.catbag.gifreduxsample;

import android.app.Application;

import com.umaplay.fluxxan.Fluxxan;

import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.reducers.AppStateReducer;

/**
 * Created by niltonvasques on 10/12/16.
 */

public class MyApp extends Application {

    private static Fluxxan<AppState> sFluxxan = null;
    private static DataManager sDataManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFluxxan();
        initializeDataManager();
    }

    private void initializeFluxxan() {
        AppState state = ImmutableAppState.builder().build();
        sFluxxan = new Fluxxan(state);
        sFluxxan.registerReducer(new AppStateReducer());
        sFluxxan.start();
    }

    private void initializeDataManager() {
        sDataManager = new DataManager(getApplicationContext());
        sDataManager.start();
    }

    public void onTerminate() {
        sFluxxan.stop();
        sDataManager.stop();
        super.onTerminate();
    }

    public static Fluxxan<AppState> getFluxxan() {
        return sFluxxan;
    }
    public static DataManager getDataManager() {
        return sDataManager;
    }
    public static void setDataManager(DataManager dataManager) {
        sDataManager = dataManager;
    }
}