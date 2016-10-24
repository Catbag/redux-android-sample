package br.com.catbag.gifreduxsample.idlings;

import android.support.test.espresso.IdlingResource;

import br.com.catbag.gifreduxsample.ui.wrappers.GlideWrapper;

public class GlideLoadIdlingResource extends GlideIdlingResource {


    public GlideLoadIdlingResource(GlideWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = mWrapper.isLoaded();
        if (idle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }
}