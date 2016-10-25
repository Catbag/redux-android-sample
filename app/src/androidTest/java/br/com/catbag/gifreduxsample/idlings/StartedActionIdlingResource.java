package br.com.catbag.gifreduxsample.idlings;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.StateListener;

import br.com.catbag.gifreduxsample.models.AppState;

public class StartedActionIdlingResource extends BaseIdlingResource implements StateListener<AppState> {

    private final Fluxxan<AppState> mFluxxan;
    private boolean mIdle = false;

    public StartedActionIdlingResource(Fluxxan<AppState> fluxxan) {
        mFluxxan = fluxxan;
        fluxxan.addListener(this);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        if (mFluxxan.getState().getGifStatus() == AppState.GifStatus.DOWNLOADING){
            mIdle = true;
        }
        return mIdle;
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        mResourceCallback.onTransitionToIdle();
        return false;
    }

    @Override
    public void onStateChanged(AppState appState) {
    }
}