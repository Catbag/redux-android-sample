package br.com.catbag.gifreduxsample.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.umaplay.fluxxan.StateListener;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;
import br.com.catbag.gifreduxsample.ui.AnvilRenderable;
import trikita.anvil.RenderableView;

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
