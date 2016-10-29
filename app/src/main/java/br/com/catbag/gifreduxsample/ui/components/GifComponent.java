package br.com.catbag.gifreduxsample.ui.components;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.models.Gif;
import pl.droidsonroids.gif.GifDrawable;
import trikita.anvil.RenderableView;

import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOADING;
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

public class GifComponent extends RenderableView {

    private Gif mGif;
    private GifDrawable mGifDrawable;

    public GifComponent(Context context) {
        super(context);
    }
    public GifComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public GifComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GifComponent withGifState(Gif gif) {
        mGif = gif;
        return this;
    }

    public GifComponent withGifDrawable(GifDrawable gifDrawable) {
        mGifDrawable = gifDrawable;
        return this;
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
            withId(R.id.gif_loading, () -> {
                visibility(mGif.getStatus() == DOWNLOADING);
            });
        });
    }

    private void setBackground() {
        if (mGif.getStatus() == NOT_DOWNLOADED && !mGif.getDownloadFailureMsg().isEmpty()) {
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
        if (mGif.getStatus() == NOT_DOWNLOADED && mGif.getDownloadFailureMsg().isEmpty()) {
            GifActionCreator.getInstance().gifDownloadStart(mGif, getContext());
        }
    }

    private void defineBehavior() {
        if (mGifDrawable != null) {
            if (mGif.getStatus() == LOOPING) {
                mGifDrawable.start();
            } else {
                mGifDrawable.stop();
            }
            onClick(v -> {
                GifActionCreator.getInstance().gifClick(mGif);
            });
        }
    }
}
