package br.com.catbag.giffluxsample.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.giffluxsample.App;
import br.com.catbag.giffluxsample.R;
import br.com.catbag.giffluxsample.actions.GifActionCreator;
import br.com.catbag.giffluxsample.models.AppState;

public class GifListActivity extends StateListenerActivity<AppState> {

    private AppState.GifStatus mGifStatus;
    private GlideDrawable mResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);


    }

    @Override
    protected Fluxxan<AppState> getFlux() {
        return App.getFluxxan();
    }

    @Override
    public void onStateChanged(AppState appState) {
        mGifStatus = appState.getGifStatus();
        switch (mGifStatus) {
            case NOT_LOADED:
                //Verify if internet is on and inform user
                loadGif(appState.getUrl());
                break;
            case LOOPING:
                ThreadUtils.runOnMain(() -> mResource.start());
                break;
            case PAUSED:
                ThreadUtils.runOnMain(() -> mResource.stop());
                break;
            default:
        }
    }

    private void loadGif(String url) {
        ImageView imageView = (ImageView) findViewById(R.id.gif_image);
        Glide.with(this)
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        GifActionCreator.getInstance().play();
                        mResource = resource;
                        return false;
                    }
                })
                .into(imageView);
        imageView.setOnClickListener(v -> GifActionCreator.getInstance().gifClick(mGifStatus));
    }
}
