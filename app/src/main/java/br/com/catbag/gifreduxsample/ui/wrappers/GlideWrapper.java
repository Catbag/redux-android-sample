package br.com.catbag.gifreduxsample.ui.wrappers;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.umaplay.fluxxan.util.ThreadUtils;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Created by felipe on 13/10/16.
 */

public class GlideWrapper {

    private static final String TAG = "GlideWrapper";
    private String mLocalPath;
    private ImageView mImageView;

    private GlideDrawable mResource;
    private GlideLoadListener mLoadListener;
    private GlideExceptionListener mExceptionListener;

    public GlideWrapper(ImageView imageView) {
        mImageView = imageView;
    }

    public void stop() {
        ThreadUtils.runOnMain(() -> mResource.stop());
    }

    public void play() {
        RequestListener listener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                       boolean isFirstResource) {
                Log.e(TAG, "", e);
                if (mExceptionListener != null) mExceptionListener.onException(e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model,
                                           Target<GlideDrawable> target, boolean isFromMemoryCache,
                                           boolean isFirstResource) {
                mResource = resource;
                return false;
            }
        };

        ThreadUtils.runOnMain(() -> {
            if (mResource != null) {
                mResource.start();
            } else {
                Glide.with(mImageView.getContext()).load(mLocalPath).listener(listener).
                        diskCacheStrategy(SOURCE).into(mImageView);
            }
        });
    }

    public void load(String localPath) {
        mLocalPath = localPath;

        RequestListener listener = new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target,
                                       boolean isFirstResource) {
                Log.e(TAG, "", e);
                if (mExceptionListener != null) mExceptionListener.onException(e);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
                                           boolean isFromMemoryCache, boolean isFirstResource) {
                if (mLoadListener != null) mLoadListener.onLoaded();
                return false;
            }
        };

        ThreadUtils.runOnMain(() -> {
            Glide.with(mImageView.getContext()).load(mLocalPath).asBitmap().listener(listener)
                    .diskCacheStrategy(ALL).into(mImageView);
        });
    }

    public GlideWrapper onLoaded(GlideLoadListener listener) {
        mLoadListener = listener;
        return this;
    }

    public GlideWrapper onException(GlideExceptionListener listener) {
        mExceptionListener = listener;
        return this;
    }

    public GlideDrawable getResource() {
        return mResource;
    }

    public interface GlideExceptionListener {
        void onException(Exception e);
    }

    public interface GlideLoadListener {
        void onLoaded();
    }
}
