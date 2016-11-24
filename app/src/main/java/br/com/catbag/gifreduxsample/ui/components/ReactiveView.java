package br.com.catbag.gifreduxsample.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import com.umaplay.fluxxan.StateListener;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;
import br.com.catbag.gifreduxsample.ui.AnvilRenderable;
import trikita.anvil.RenderableView;

/**
 * Created by raulcca on 11/21/16.
 */

public abstract class ReactiveView extends RenderableView
        implements StateListener<AppState>, AnvilRenderable {

    private boolean mIsRegisteredOnStateChange = false;
    private AnvilRenderListener mAnvilRenderListener;

    public ReactiveView(Context context) {
        super(context);
        initialState();
    }

    public ReactiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialState();
    }

    public ReactiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialState();
    }

    protected void initialState() {
        onRegisterOnStateChange();
    }

    @Override
    public void setAnvilRenderListener(AnvilRenderListener listener) {
        mAnvilRenderListener = listener;
    }

    protected void onAnvilRendered() {
        if (mAnvilRenderListener != null) mAnvilRenderListener.onAnvilRendered();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            onRegisterOnStateChange();
        } else {
            onUnregisterOnStateChange();
        }
    }

    protected void onRegisterOnStateChange() {
        if (mIsRegisteredOnStateChange) return;

        mIsRegisteredOnStateChange = true;
        MyApp.getFluxxan().addListener(this);
    }

    protected void onUnregisterOnStateChange() {
        mIsRegisteredOnStateChange = false;
        MyApp.getFluxxan().removeListener(this);
    }
}
