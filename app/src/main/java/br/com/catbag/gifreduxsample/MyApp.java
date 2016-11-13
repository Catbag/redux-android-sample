package br.com.catbag.gifreduxsample;

import android.app.Application;


import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.Middleware;

import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.asyncs.net.downloader.FileDownloader;
import br.com.catbag.gifreduxsample.middlewares.LoggerMiddleware;
import br.com.catbag.gifreduxsample.middlewares.RestMiddleware;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.reducers.AppStateReducer;

/**
 * Created by niltonvasques on 10/12/16.
 */

public class MyApp extends Application {

    private static Fluxxan<AppState> sFluxxan = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFluxxan();
    }

    private void initializeFluxxan() {
        AppState state = ImmutableAppState.builder().build();
        sFluxxan = new Fluxxan(state);
        sFluxxan.registerReducer(new AppStateReducer());
        sFluxxan.getDispatcher().registerMiddleware(new LoggerMiddleware());

        Middleware restMiddleware = new RestMiddleware(getBaseContext(),
                new DataManager(), new FileDownloader());
        sFluxxan.getDispatcher().registerMiddleware(restMiddleware);
        sFluxxan.start();
    }

    public void onTerminate() {
        sFluxxan.stop();
        super.onTerminate();
    }

    public static Fluxxan<AppState> getFluxxan() {
        return sFluxxan;
    }
}
