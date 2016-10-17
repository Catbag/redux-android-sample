package br.com.catbag.gifreduxsample.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.gifreduxsample.App;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.ui.wrappers.GlideWrapper;
import trikita.anvil.Anvil;

import static trikita.anvil.DSL.backgroundColor;
import static trikita.anvil.DSL.onClick;
import static trikita.anvil.DSL.visibility;
import static trikita.anvil.DSL.withId;

public class GifListActivity extends StateListenerActivity<AppState> {

    private AppState mAppState = getFlux().getState();
    private GifActionCreator mActionCreator = GifActionCreator.getInstance();

    //Views
    private GlideWrapper mGlideWrapper;

    //Bindings
    private boolean mGifProgressVisibility;
    private int mGifBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);
        bindingViews();
        initializeGifView();
        mActionCreator.gifDownloadStart(mAppState.getGifUrl(), mAppState.getGifTitle(), this);
    }

    private void bindingViews() {
        //Bindings Defaults
        mGifBackgroundColor = ContextCompat.getColor(this, R.color.notWatched);
        mGifProgressVisibility = false;

        Anvil.mount(findViewById(R.id.activity_gif_list), () -> {
            backgroundColor(mGifBackgroundColor);

            withId(R.id.loading, () -> {
                visibility(mGifProgressVisibility);
            });
            withId(R.id.gif_image, () -> {
                onClick(v -> mActionCreator.gifClick(mAppState.getGifStatus()));
            });
        });
    }

    @Override
    protected Fluxxan<AppState> getFlux() {
        return App.getFluxxan();
    }

    @Override
    public void onStateChanged(AppState appState) {
        mAppState = appState;
        switch (mAppState.getGifStatus()) {
            case NOT_DOWNLOADED:
                String errorMsg = appState.getGifDownloadFailureMsg();
                if (!errorMsg.isEmpty()) {
                    showToast(errorMsg);
                    mGifProgressVisibility = false;
                }
                break;
            case DOWNLOADING:
                mGifProgressVisibility = true;
                break;
            case DOWNLOADED:
                mGlideWrapper.load(appState.getGifLocalPath());
                break;
            case LOOPING:
                mGlideWrapper.play();
                break;
            case PAUSED:
                mGlideWrapper.stop();
                break;
            default:
        }

        if (appState.getGifWatched()) {
            mGifBackgroundColor = ContextCompat.getColor(this, R.color.watched);
        }

        Anvil.render();
    }

    public GlideWrapper getGlideWrapper() {
        return mGlideWrapper;
    }

    private void showToast(String msg) {
        ThreadUtils.runOnMain(() -> {
            Toast.makeText(GifListActivity.this, msg, Toast.LENGTH_LONG).show();
        });
    }

    private void initializeGifView() {
        ImageView imageView = (ImageView) findViewById(R.id.gif_image);
        mGlideWrapper = new GlideWrapper(imageView)
                .onException((e) -> showToast(e.getMessage()))
                .onLoaded(() -> {
                    mGifProgressVisibility = false;
                    Anvil.render();
                });
    }
}
