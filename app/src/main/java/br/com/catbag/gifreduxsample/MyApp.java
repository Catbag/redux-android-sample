package br.com.catbag.gifreduxsample;

import android.app.Application;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.Middleware;

import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.asyncs.net.downloader.FileDownloader;
import br.com.catbag.gifreduxsample.middlewares.PersistenceMiddleware;
import br.com.catbag.gifreduxsample.middlewares.RestMiddleware;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.reducers.AppStateReducer;

/**
 * Created by niltonvasques on 10/12/16.
 */

public class MyApp extends Application {

    private static Fluxxan<AppState> sFluxxan = null;
    private DataManager mDataManager = null;
    private PersistenceMiddleware mPersistenceMiddleware = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDataManager();
        initializeFluxxan();
    }

    private void initializeDataManager() {
        mDataManager = new DataManager(getBaseContext());
    }

    private void initializeFluxxan() {
        AppState state = ImmutableAppState.builder().build();
        sFluxxan = new MyFluxxan(state);
        sFluxxan.registerReducer(new AppStateReducer());

        Middleware restMiddleware = new RestMiddleware(getBaseContext(), mDataManager,
                new FileDownloader());
        sFluxxan.getDispatcher().registerMiddleware(restMiddleware);

        mPersistenceMiddleware = new PersistenceMiddleware(mDataManager);
        sFluxxan.getDispatcher().registerMiddleware(mPersistenceMiddleware);
        sFluxxan.addListener(mPersistenceMiddleware);
        sFluxxan.start();
    }

    public void onTerminate() {
        sFluxxan.removeListener(mPersistenceMiddleware);
        sFluxxan.stop();
        super.onTerminate();
    }

    public static Fluxxan<AppState> getFluxxan() {
        return sFluxxan;
    }
}
