package br.com.catbag.gifreduxsample.idlings;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import com.umaplay.fluxxan.StateListener;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.ui.GifListActivity;

/**
 * Created by niltonvasques on 10/24/16.
 */

public class UiTestLocker implements IdlingResource, StateListener<AppState>{

    private final GifListActivity mActivity;

    protected ResourceCallback mResourceCallback;

    private boolean mStateChanged = false;

    public UiTestLocker(GifListActivity activity){
        mActivity = activity;
        MyApp.getFluxxan().addListener(this);
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    @Override
    public String getName() {
        return UiTestLocker.class.getClass().getSimpleName();
    }

    /**
     * Is important that onStateChanged inside activity control when the Anvil finalize all renders,
     * eg. putting both flag and the Anvil.render(); inside same thread like GifListActivity does.
     **/
    @Override
    public boolean isIdleNow() {
        boolean idle = (mStateChanged && mActivity.wasRendered());
        if (idle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    /** Register idling resources don't stop flow when junit asserts is used **/
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(this);
    }

    public void unregisterIdlingResource(){
        Espresso.unregisterIdlingResources(this);
        MyApp.getFluxxan().removeListener(this);
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        mStateChanged = true;
        return false;
    }

    @Override
    public void onStateChanged(AppState appState) {

    }
}
