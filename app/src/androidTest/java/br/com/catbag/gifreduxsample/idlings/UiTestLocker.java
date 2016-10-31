package br.com.catbag.gifreduxsample.idlings;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import br.com.catbag.gifreduxsample.ui.AnvilRenderComponent;
import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;

/**
 * Created by niltonvasques on 10/24/16.
 */

public class UiTestLocker implements IdlingResource, AnvilRenderListener {

    private AnvilRenderComponent mAnvilRenderComponent;
    private boolean mIdle;
    protected ResourceCallback mResourceCallback;

    public UiTestLocker(AnvilRenderComponent anvilRenderComponent){
        mAnvilRenderComponent = anvilRenderComponent;
        mAnvilRenderComponent.setAnvilRenderListener(this);
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
        if (mIdle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return mIdle;
    }

    /** Register idling resources don't stop flow when junit asserts is used **/
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(this);
    }

    public void unregisterIdlingResource(){
        Espresso.unregisterIdlingResources(this);
        mAnvilRenderComponent.setAnvilRenderListener(null);
        mIdle = false;
    }

    @Override
    public void onAnvilRendered() {
        mIdle = true;
    }
}
