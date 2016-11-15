package br.com.catbag.gifreduxsample.idlings;

import android.os.Handler;
import android.os.Looper;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import br.com.catbag.gifreduxsample.ui.AnvilRenderComponent;
import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;

/**
 * Created by niltonvasques on 10/24/16.
 */

public class AnvilTestLocker implements IdlingResource, AnvilRenderListener {

    private AnvilRenderComponent mAnvilRenderComponent;
    private ResourceCallback mResourceCallback;
    private int mRenderTimes = 0;
    private int mLastRenderTimes = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mForceIsIdleNow = () -> isIdleNow();

    public AnvilTestLocker(AnvilRenderComponent anvilRenderComponent){
        mAnvilRenderComponent = anvilRenderComponent;
        mAnvilRenderComponent.setAnvilRenderListener(this);
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    @Override
    public String getName() {
        return AnvilTestLocker.class.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        mHandler.removeCallbacks(mForceIsIdleNow);
        boolean isIdle = mRenderTimes != 0 && mRenderTimes == mLastRenderTimes;
        mLastRenderTimes = mRenderTimes;

        if (mResourceCallback != null) {
            if (isIdle) {
                mResourceCallback.onTransitionToIdle();
            } else {
                mHandler.postDelayed(mForceIsIdleNow, 250);
            }
        }
        return isIdle;
    }

    /** Register idling resources don't stop flow when junit asserts is used **/
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(this);
    }

    public void unregisterIdlingResource(){
        mHandler.removeCallbacks(mForceIsIdleNow);
        mAnvilRenderComponent.setAnvilRenderListener(null);
        Espresso.unregisterIdlingResources(this);
    }

    @Override
    public void onAnvilRendered() {
        mRenderTimes++;
    }
}