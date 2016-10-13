package br.com.catbag.giffluxsample.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.giffluxsample.App;
import br.com.catbag.giffluxsample.R;
import br.com.catbag.giffluxsample.actions.GifActionCreator;
import br.com.catbag.giffluxsample.models.AppState;
import br.com.catbag.giffluxsample.ui.wrappers.GlideWrapper;

public class GifListActivity extends StateListenerActivity<AppState> {

    private AppState mAppState = getFlux().getState();
    private GifActionCreator mActionCreator = GifActionCreator.getInstance();
    private ProgressBar mLoading;
    private GlideWrapper mGlideWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);
        initializeGifView();
        mLoading = (ProgressBar) findViewById(R.id.loading);
        mActionCreator.gifDownloadStart(mAppState.getGifUrl(), mAppState.getGifTitle(), this);
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
                    mLoading.setVisibility(View.GONE);
                }
                break;
            case DOWNLOADING:
                setVisibilityLoading(View.VISIBLE);
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
    }

    private void setVisibilityLoading(int visibility) {
        ThreadUtils.runOnMain(() -> mLoading.setVisibility(visibility));
    }

    private void showToast(String msg) {
        ThreadUtils.runOnMain(() -> {
            Toast.makeText(GifListActivity.this, msg, Toast.LENGTH_LONG).show();
        });
    }

    private void initializeGifView() {
        ImageView imageView = (ImageView) findViewById(R.id.gif_image);
        imageView.setOnClickListener(v -> mActionCreator.gifClick(mAppState.getGifStatus()));
        mGlideWrapper = new GlideWrapper(imageView)
                .onException((e) -> showToast(e.getLocalizedMessage()))
                .onLoaded(() -> setVisibilityLoading(View.GONE));
    }
}
