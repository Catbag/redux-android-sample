package br.com.catbag.gifreduxsample.lockers;

import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;
import br.com.catbag.gifreduxsample.ui.AnvilRenderable;

/**
 * Created by niltonvasques on 10/24/16.
 */

public class AnvilTestLocker extends BaseTestLocker implements AnvilRenderListener {

    private AnvilRenderable mAnvilRenderable;
    private int mRenderTimes = 0;
    private int mLastRenderTimes = 0;

    public AnvilTestLocker(AnvilRenderable anvilRenderable) {
        mAnvilRenderable = anvilRenderable;
        mAnvilRenderable.setAnvilRenderListener(this);
    }

    @Override
    protected boolean isIdle() {
        boolean isIdle = mRenderTimes != 0 && mRenderTimes == mLastRenderTimes;
        mLastRenderTimes = mRenderTimes;

        return isIdle;
    }

    @Override
    public void unregisterIdlingResource() {
        mAnvilRenderable.setAnvilRenderListener(null);
        super.unregisterIdlingResource();
    }

    @Override
    public void onAnvilRendered() {
        mRenderTimes++;
    }
}