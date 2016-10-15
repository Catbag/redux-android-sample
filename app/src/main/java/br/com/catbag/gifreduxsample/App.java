package br.com.catbag.gifreduxsample;

import android.app.Application;

import com.umaplay.fluxxan.Fluxxan;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.reducers.GifReducer;

/**
 * Created by niltonvasques on 10/12/16.
 */

public class App extends Application {

    private static Fluxxan<AppState> sFluxxan = null;

    public static Fluxxan<AppState> getFluxxan() {
        return sFluxxan;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFluxxan();
    }

    private void initializeFluxxan() {
        AppState state = ImmutableAppState.builder().build();
        sFluxxan = new Fluxxan<>(state);
        sFluxxan.registerReducer(new GifReducer());
        sFluxxan.start();
    }

    public void onTerminate() {
        sFluxxan.stop();
        super.onTerminate();
    }
}
