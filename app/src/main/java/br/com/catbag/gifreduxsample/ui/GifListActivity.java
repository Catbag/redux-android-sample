package br.com.catbag.gifreduxsample.ui;

import android.os.Bundle;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.AppStateActionCreator;
import br.com.catbag.gifreduxsample.models.AppState;
import trikita.anvil.Anvil;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.DSL.visibility;

public class GifListActivity extends StateListenerActivity<AppState>
        implements AnvilRenderComponent {

    //Binding Data
    private boolean mGifProgressVisibility = true;
    private AnvilRenderListener mAnvilRenderListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);
        bindingViews();
        AppStateActionCreator.getInstance().loadAppState();
    }

    @Override
    protected Fluxxan<AppState> getFlux() {
        return MyApp.getFluxxan();
    }

    @Override
    public void onStateChanged(AppState appState) {
        if (!appState.getGifs().isEmpty()) {
            mGifProgressVisibility = false;
        }
        //TODO this can be controlled by a singleton class that manage broadcasters and listeners
        ThreadUtils.runOnMain(() -> {
            Anvil.render();
            if (mAnvilRenderListener != null) mAnvilRenderListener.onAnvilRendered();
        });
    }

    @Override
    public void setAnvilRenderListener(AnvilRenderListener listener) {
        mAnvilRenderListener = listener;
    }

    private void bindingViews() {
        //Bindings Defaults
        Anvil.mount(findViewById(R.id.activity_gif_list), () -> {
            withId(R.id.loading, () -> {
                visibility(mGifProgressVisibility);
            });
        });
    }
}