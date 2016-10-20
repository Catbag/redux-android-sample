package br.com.catbag.gifreduxsample.ui;

import android.os.Bundle;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.helpers.AppStateHelper;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.ui.components.FeedComponent;
import trikita.anvil.Anvil;

import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.DSL.visibility;

public class GifListActivity extends StateListenerActivity<AppState> {

    private boolean mRendered = false;

    //Redux components
    private GifListActionCreator mActionCreator = GifListActionCreator.getInstance();

    //Binding Data
    private boolean mGifProgressVisibility = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);
        bindingViews();
        mActionCreator.loadGifs();
    }

    private void bindingViews() {
        //Bindings Defaults
        Anvil.mount(findViewById(R.id.activity_gif_list), () -> {
            withId(R.id.loading, () -> {
                visibility(mGifProgressVisibility);
            });
        });
    }

    @Override
    protected Fluxxan<AppState> getFlux() {
        return MyApp.getFluxxan();
    }

    @Override
    public void onStateChanged(AppState appState) {
        mRendered = false;
        FeedComponent feed = (FeedComponent) findViewById(R.id.feed);
        feed.setGifs(AppStateHelper.getListGifs(appState));
        if (!appState.getGifs().isEmpty()) {
            mGifProgressVisibility = false;
        }

        ThreadUtils.runOnMain(() -> {
            Anvil.render();
            mRendered = true;
        });
    }

    public boolean wasRendered() {
        return mRendered;
    }
}
