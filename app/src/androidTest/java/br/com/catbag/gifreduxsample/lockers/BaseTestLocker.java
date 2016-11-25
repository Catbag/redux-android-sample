package br.com.catbag.gifreduxsample.lockers;

import android.os.Handler;
import android.os.Looper;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by raulcca on 11/25/16.
 */

public abstract class BaseTestLocker implements IdlingResource {

    private static final int FORCE_IS_IDLE_NOW_DELAY = 250;
    private ResourceCallback mResourceCallback;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mForceIsIdleNow = this::isIdleNow;

    protected abstract boolean isIdle();

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        mHandler.removeCallbacks(mForceIsIdleNow);
        boolean isIdle = isIdle();

        if (mResourceCallback != null) {
            if (isIdle) {
                mResourceCallback.onTransitionToIdle();
            } else {
                mHandler.postDelayed(mForceIsIdleNow, FORCE_IS_IDLE_NOW_DELAY);
            }
        }
        return isIdle;
    }

    /** Register idling resources don't stop flow when junit asserts is used **/
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(this);
    }

    public void unregisterIdlingResource() {
        mHandler.removeCallbacks(mForceIsIdleNow);
        Espresso.unregisterIdlingResources(this);
    }

    public void waitForEspresso() {
        registerIdlingResource();
        onView(withText("espressoWaiter")).check(doesNotExist());
        unregisterIdlingResource();
    }
}
