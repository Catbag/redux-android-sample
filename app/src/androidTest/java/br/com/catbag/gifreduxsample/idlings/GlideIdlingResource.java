package br.com.catbag.gifreduxsample.idlings;

import br.com.catbag.gifreduxsample.ui.wrappers.GlideWrapper;

/**
 * Created by niltonvasques on 10/24/16.
 */
public abstract class GlideIdlingResource extends BaseIdlingResource {
    protected final GlideWrapper mWrapper;

    public GlideIdlingResource(GlideWrapper wrapper) {
        mWrapper = wrapper;
    }
}
