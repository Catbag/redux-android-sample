package br.com.catbag.gifreduxsample;

import android.support.annotation.NonNull;

import com.umaplay.fluxxan.Dispatcher;
import com.umaplay.fluxxan.Fluxxan;

import br.com.catbag.gifreduxsample.models.AppState;

/**
 * Created by raulcca on 11/21/16.
 */

public class MyFluxxan extends Fluxxan<AppState> {
    /**
     * Create a new instance
     *
     * @param state The initial state tree
     */
    public MyFluxxan(@NonNull AppState state) {
        super(state);
    }

    @Override
    protected Dispatcher<AppState> initDispatcher(AppState state) {
        return new MyDispatcherImpl(state);
    }
}
