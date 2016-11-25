package br.com.catbag.gifreduxsample.ui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import pl.droidsonroids.gif.GifDrawable;

import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOADING;
import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOAD_FAILED;
import static br.com.catbag.gifreduxsample.models.Gif.Status.LOOPING;
import static br.com.catbag.gifreduxsample.models.Gif.Status.NOT_DOWNLOADED;
import static trikita.anvil.BaseDSL.visibility;
import static trikita.anvil.BaseDSL.withId;
import static trikita.anvil.BaseDSL.xml;
import static trikita.anvil.DSL.backgroundColor;
import static trikita.anvil.DSL.imageDrawable;
import static trikita.anvil.DSL.onClick;

/**
 * Created by felipe on 26/10/16.
 */

public class GifView extends ReactiveView {

    private Gif mGif;
    private GifDrawable mGifDrawable;
    private boolean mHasRequestedDownload = false;

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GifView withGif(Gif gif) {
        if (gif != null) {
            if (!gif.getUuid().equals(mGif.getUuid())) mHasRequestedDownload = false;
            mGif = gif;
        }
        return this;
    }

    public GifView withGifDrawable(GifDrawable gifDrawable) {
        mGifDrawable = gifDrawable;
        return this;
    }

    public Gif getGif() {
        return mGif;
    }

    @Override
    public void view() {
        xml(R.layout.gif_item, () -> {
            setBackground();
            withId(R.id.gif_image, () -> {
                imageDrawable(mGifDrawable);
                requestContent();
                defineBehavior();
            });
            withId(R.id.gif_loading, () -> visibility(mGif.getStatus() == DOWNLOADING));
        });
        onAnvilRendered();
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        Gif newGif = newState.getGifs().get(mGif.getUuid());
        return newGif != null && !newGif.equals(mGif);
    }

    @Override
    public void onStateChanged(AppState appState) {
        withGif(appState.getGifs().get(mGif.getUuid()));

        if (mGif.getStatus() == DOWNLOAD_FAILED) {
            mHasRequestedDownload = false;
        }
    }

    @Override
    protected void initialState() {
        super.initialState();
        mGif = ImmutableGif.builder().url("").path("").title("").uuid("")
                .build();
    }

    private void setBackground() {
        if (mGif.getStatus() == DOWNLOAD_FAILED) {
            backgroundColor(ContextCompat.getColor(getContext(), R.color.error));
            return;
        }

        if (mGif.getWatched()) {
            backgroundColor(ContextCompat.getColor(getContext(), R.color.watched));
        } else {
            backgroundColor(ContextCompat.getColor(getContext(), R.color.notWatched));
        }
    }

    private void requestContent() {
        if (mGif.getStatus() == NOT_DOWNLOADED && !mHasRequestedDownload) {
            mHasRequestedDownload = true;
            GifActionCreator.getInstance().gifDownloadStart(mGif);
        }
    }

    private void defineBehavior() {
        if (mGifDrawable != null) {
            if (mGif.getStatus() == LOOPING) {
                mGifDrawable.start();
            } else {

                mGifDrawable.stop();
            }
            onClick(v -> GifActionCreator.getInstance().gifClick(mGif));
        }
    }
}
