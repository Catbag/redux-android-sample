package br.com.catbag.giffluxsample.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.giffluxsample.App;
import br.com.catbag.giffluxsample.R;
import br.com.catbag.giffluxsample.actions.GifActionCreator;
import br.com.catbag.giffluxsample.models.AppState;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

public class GifListActivity extends StateListenerActivity<AppState> {

    private AppState mAppState = getFlux().getState();
    private GifActionCreator mActionCreator = GifActionCreator.getInstance();
    private ImageView mGifView;
    private ProgressBar mLoading;
    private GlideDrawable mResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);
        mGifView = (ImageView) findViewById(R.id.gif_image);
        mGifView.setOnClickListener(v -> mActionCreator.gifClick(mAppState.getGifStatus()));
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
                if(!errorMsg.isEmpty()) {
                    showToast(errorMsg);
                    mLoading.setVisibility(View.GONE);
                }
                break;
            case DOWNLOADING:
                setVisibilityLoading(View.VISIBLE);
                break;
            case DOWNLOADED:
                setVisibilityLoading(View.GONE);
                loadGif(appState.getGifLocalPath());
                break;
            case LOOPING:
                playGif(appState.getGifLocalPath());
                break;
            case PAUSED:
                stopGif();
                break;
            default:
        }
    }

    private void setVisibilityLoading(int visibility) {
        ThreadUtils.runOnMain(() -> mLoading.setVisibility(visibility));
    }

    private void stopGif() {
        ThreadUtils.runOnMain(() -> mResource.stop());
    }

    private void playGif(String gifLocalPath) {
        RequestListener listener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                       boolean isFirstResource) {
                System.out.println("GifListActivity.onException: " + e.getLocalizedMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model,
                                           Target<GlideDrawable> target, boolean isFromMemoryCache,
                                           boolean isFirstResource) {
                System.out.println("GifListActivity.onResourceReady");
                mResource = resource;
                return false;
            }
        };

        ThreadUtils.runOnMain(() -> {
            if(mResource != null) {
                mResource.start();
            }else{
                Glide.with(this).load(gifLocalPath)
                     .listener(listener).diskCacheStrategy(SOURCE).into(mGifView);
            }
        });
    }

    private void loadGif(String gifLocalPath) {
        ThreadUtils.runOnMain(() -> {
            System.out.println("GifListActivity.loadGif: " + gifLocalPath);
            Glide.with(this).load(gifLocalPath).asBitmap().diskCacheStrategy(ALL).into(mGifView);
        });
    }

    private void showToast(String msg){
        ThreadUtils.runOnMain(() -> {
            Toast.makeText(GifListActivity.this, msg, Toast.LENGTH_LONG).show();
        });
    }
}
