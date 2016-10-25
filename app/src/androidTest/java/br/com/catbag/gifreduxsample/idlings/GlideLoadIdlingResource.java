package br.com.catbag.gifreduxsample.idlings;

import br.com.catbag.gifreduxsample.ui.wrappers.GifWrapper;

public class GlideLoadIdlingResource extends GlideIdlingResource {


    public GlideLoadIdlingResource(GifWrapper wrapper) {
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