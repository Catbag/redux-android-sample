package br.com.catbag.gifreduxsample.ui;

import android.os.Bundle;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.AppStateActionCreator;
import br.com.catbag.gifreduxsample.models.AppState;
import trikita.anvil.Anvil;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.DSL.visibility;

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

public class GifListActivity extends StateListenerActivity<AppState>
        implements AnvilRenderable {

    //Binding Data
    private boolean mGifProgressVisibility;
    private AnvilRenderListener mAnvilRenderListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);
        initialState();
        bindingViews();
    }

    private void initialState() {
        mGifProgressVisibility = MyApp.getFluxxan().getState().getGifs().isEmpty();
    }

    @Override
    protected Fluxxan<AppState> getFlux() {
        return MyApp.getFluxxan();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppStateActionCreator.getInstance().loadAppState();
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        return !newState.equals(oldState);
    }

    @Override
    public void onStateChanged(AppState appState) {
        if (!appState.getGifs().isEmpty()) {
            mGifProgressVisibility = false;
        }
    }

    @Override
    public void setAnvilRenderListener(AnvilRenderListener listener) {
        mAnvilRenderListener = listener;
    }

    private void bindingViews() {
        //Bindings Defaults
        Anvil.mount(findViewById(R.id.activity_gif_list), () -> {
            withId(R.id.loading, () -> visibility(mGifProgressVisibility));

            if (mAnvilRenderListener != null) mAnvilRenderListener.onAnvilRendered();
        });
    }
}
