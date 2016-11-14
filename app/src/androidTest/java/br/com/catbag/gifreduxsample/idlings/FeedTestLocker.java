package br.com.catbag.gifreduxsample.idlings;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import br.com.catbag.gifreduxsample.ui.AnvilRenderComponent;
import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;

/**
 * Created by niltonvasques on 10/24/16.
 */

public class FeedTestLocker implements IdlingResource, AnvilRenderListener {

    private int mUntilRenderTimes;
    private int mRenderTimes = 0;
    private AnvilRenderComponent mAnvilRenderComponent;
    protected ResourceCallback mResourceCallback;

    public FeedTestLocker(AnvilRenderComponent anvilRenderComponent, int untilRenderTimes){
        mAnvilRenderComponent = anvilRenderComponent;
        mUntilRenderTimes = untilRenderTimes;
        mAnvilRenderComponent.setAnvilRenderListener(this);
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    @Override
    public String getName() {
        return FeedTestLocker.class.getClass().getSimpleName();
    }

    /**
     * Is important that onStateChanged inside activity control when the Anvil finalize all renders,
     * eg. putting both flag and the Anvil.render(); inside same thread like GifListActivity does.
     **/
    @Override
    public boolean isIdleNow() {
        boolean isIdle = mRenderTimes == mUntilRenderTimes;
        if (isIdle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return isIdle;
    }

    /** Register idling resources don't stop flow when junit asserts is used **/
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(this);
    }

    public void unregisterIdlingResource(){
        Espresso.unregisterIdlingResources(this);
        mAnvilRenderComponent.setAnvilRenderListener(null);
        mRenderTimes = 0;
    }

    @Override
    public void onAnvilRendered() {
        mRenderTimes++;
    }
}
