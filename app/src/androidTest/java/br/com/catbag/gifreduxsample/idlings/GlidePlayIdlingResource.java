package br.com.catbag.gifreduxsample.idlings;

import android.support.test.espresso.IdlingResource;

import br.com.catbag.gifreduxsample.ui.wrappers.GifWrapper;

public class GlidePlayIdlingResource extends GlideIdlingResource implements IdlingResource {

    public GlidePlayIdlingResource(GifWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = mWrapper.getDrawable() != null;
        if (idle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return idle;
    }
}