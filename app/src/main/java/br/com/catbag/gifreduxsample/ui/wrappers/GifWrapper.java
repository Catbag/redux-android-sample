package br.com.catbag.gifreduxsample.ui.wrappers;

import com.umaplay.fluxxan.util.ThreadUtils;

import java.io.IOException;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by felipe on 13/10/16.
 */

public class GifWrapper {

    private static final String TAG = "GifWrapper";
    private String mLocalPath;

    private GifImageView mImageView;
    private GifDrawable mDrawable;

    private GlideLoadListener mLoadListener;
    private GlideExceptionListener mExceptionListener;

    private boolean mLoaded = false;

    public GifWrapper(GifImageView imageView) {
        mImageView = imageView;
    }

    public void stop() {
        if(mDrawable != null && mDrawable.canPause()){
            mDrawable.pause();
        }
    }

    public void play() {
        if(mDrawable != null){
            mDrawable.start();
        }
    }

    public void load(String localPath) {
        mLocalPath = localPath;

        try {
            mDrawable = new GifDrawable(localPath);
            mDrawable.stop();
            ThreadUtils.runOnMain(() -> {
                mImageView.setImageDrawable(mDrawable);
                mLoaded = true;
                if(mLoadListener != null) mLoadListener.onLoaded();
            });
        } catch (IOException e) {
            if(mExceptionListener != null) mExceptionListener.onException(e);
        }
    }

    public GifWrapper onLoaded(GlideLoadListener listener) {
        mLoadListener = listener;
        return this;
    }

    public GifWrapper onException(GlideExceptionListener listener) {
        mExceptionListener = listener;
        return this;
    }

    public boolean isLoaded() {
        return mLoaded;
    }


    public GifDrawable getDrawable() {
        return mDrawable;
    }

    public interface GlideExceptionListener {
        void onException(Exception e);
    }

    public interface GlideLoadListener {
        void onLoaded();
    }
}