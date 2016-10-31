package br.com.catbag.gifreduxsample.ui.components;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.umaplay.fluxxan.StateListener;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.helpers.AppStateHelper;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import br.com.catbag.gifreduxsample.ui.AnvilRenderComponent;
import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;
import pl.droidsonroids.gif.GifDrawable;
import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

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

public class GifComponent extends RenderableView implements StateListener<AppState>, AnvilRenderComponent {

    private Gif mGif;
    private GifDrawable mGifDrawable;
    private AnvilRenderListener mAnvilRenderListener;
    private boolean mIsRegisteredOnStateChange = false;

    public GifComponent(Context context) {
        super(context);
        initialState();
    }

    public GifComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialState();
    }

    public GifComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialState();
    }

    public GifComponent withGifState(Gif gif) {
        if (gif != null) {
            mGif = gif;
        }
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

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        return newState.getGifs().get(mGif.getUuid()) != mGif;
    }

    @Override
    public void onStateChanged(AppState appState) {
        withGifState(AppStateHelper.getGifStateByUuid(mGif.getUuid(), appState));
        ThreadUtils.runOnMain(() -> {
            Anvil.render();
            if (mAnvilRenderListener != null) mAnvilRenderListener.onAnvilRendered();
        });
    }

    @Override
    public void setAnvilRenderListener(AnvilRenderListener listener) {
        mAnvilRenderListener = listener;
    }

    private void initialState() {
        mGif = ImmutableGif.builder().url("").path("").title("").uuid("")
                .build();

        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                registerOnStateChange();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                unregisterOnStateChange();
            }
        });
    }

    private void registerOnStateChange() {
        if (mIsRegisteredOnStateChange) return;

        mIsRegisteredOnStateChange = true;
        MyApp.getFluxxan().addListener(this);
        onStateChanged(MyApp.getFluxxan().getState()); //let's refesh the ui
    }

    private void unregisterOnStateChange() {
        mIsRegisteredOnStateChange = false;
        MyApp.getFluxxan().removeListener(this);
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
        if (mGif.getStatus() == NOT_DOWNLOADED) {
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
