package br.com.catbag.gifreduxsample.lockers;

import android.os.Handler;
import android.os.Looper;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 Copyright 26/10/2016
 Felipe Piñeiro (fpbitencourt@gmail.com),
 Nilton Vasques (nilton.vasques@gmail.com) and
 Raul Abreu (raulccabreu@gmail.com)

 Be free to ask for help, email us!

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/

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
