package br.com.catbag.gifreduxsample.idlings;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

/**
 * Created by niltonvasques on 10/24/16.
 */

public abstract class BaseIdlingResource implements IdlingResource{

    protected ResourceCallback mResourceCallback;

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    public void registerIdlingResource() {
        Espresso.registerIdlingResources(this);
    }

    public void unregisterIdlingResource(){
        Espresso.unregisterIdlingResources(this);
    }
}
