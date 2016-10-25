package br.com.catbag.gifreduxsample.idlings;

import br.com.catbag.gifreduxsample.ui.wrappers.GifWrapper;

/**
 * Created by niltonvasques on 10/24/16.
 */
public abstract class GlideIdlingResource extends BaseIdlingResource {
    protected final GifWrapper mWrapper;

    public GlideIdlingResource(GifWrapper wrapper) {
        mWrapper = wrapper;
    }
}
