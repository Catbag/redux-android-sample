package br.com.catbag.gifreduxsample.ui.components;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.umaplay.fluxxan.StateListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.helpers.AppStateHelper;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.ui.AnvilRenderComponent;
import br.com.catbag.gifreduxsample.ui.AnvilRenderListener;
import pl.droidsonroids.gif.GifDrawable;
import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;
import trikita.anvil.recyclerview.Recycler;

import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOADING;
import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOAD_FAILED;
import static br.com.catbag.gifreduxsample.models.Gif.Status.NOT_DOWNLOADED;
import static trikita.anvil.BaseDSL.v;

/**
 * Created by niltonvasques on 10/26/16.
 */
public class FeedComponent extends RenderableView implements StateListener<AppState>, AnvilRenderComponent {

    private List<Gif> mGifs;
    private DrawableCache mDrawables = new DrawableCache();
    private LinearLayoutManager mLayoutManager;
    private AnvilRenderListener mAnvilRenderListener;
    private boolean mIsRegisteredOnStateChange = false;

    public FeedComponent(Context context) {
        super(context);
        initialState();
    }

    public FeedComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialState();
    }

    public FeedComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialState();
    }

    @Override
    public void view() {
        Recycler.view(() -> {
            if (mLayoutManager == null) {
                mLayoutManager = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false);
            }
            Recycler.layoutManager(mLayoutManager);
            Recycler.hasFixedSize(true);
            Recycler.adapter(Recycler.Adapter.simple(mGifs, (viewHolder) -> {
                renderGifView(mGifs.get(viewHolder.getAdapterPosition()));
            }));
        });
        if (mAnvilRenderListener != null) mAnvilRenderListener.onAnvilRendered();
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        return newState.getGifs() != mGifs;
    }

    @Override
    public void onStateChanged(AppState appState) {
        mGifs = AppStateHelper.extractGifList(appState);
        Anvil.render();
    }

    @Override
    public void setAnvilRenderListener(AnvilRenderListener listener) {
        mAnvilRenderListener = listener;
    }

    private void initialState() {
        mGifs = new ArrayList<>();

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

    private void renderGifView(Gif gif) {
        v(GifComponent.class, () -> {
            ((GifComponent) Anvil.currentView())
                    .withGifDrawable(createDrawable(gif))
                    .withGifState(gif);
        });
    }

    private GifDrawable createDrawable(Gif gif) {
        GifDrawable drawable = null;
        try {
            if (mDrawables.containsKey(gif.getUuid())) {
                drawable = mDrawables.get(gif.getUuid());
            } else if (gif.getStatus() != NOT_DOWNLOADED
                    && gif.getStatus() != DOWNLOADING
                    && gif.getStatus() != DOWNLOAD_FAILED) {
                drawable = new GifDrawable(gif.getPath());
                mDrawables.put(gif.getUuid(), drawable);
            }
        } catch (IOException e) {
            Log.e("Feed", gif.toString(), e);
        }

        return drawable;
    }

    private class DrawableCache extends LinkedHashMap<String, GifDrawable> {
        private static final int CACHE_CAPACITY = 10;
        /* The load factor indicates the size where the linked hash will trigger to
         * increase his size. */
        private static final float LOAD_FACTOR = 0.7f;
        /* The initial capacity should be a little bit greater than cache capacity to keep
         * a threshold that ensures that removeEldest will be triggered before the list be full.
         */
        private static final int INITIAL_CAPACITY = (int) (CACHE_CAPACITY * LOAD_FACTOR);
        /* The accessOrder ensures that eldestEntry will be the most oldest entry accessed.
         * This fits well in the feed, since the eldest gif accessed usually will be
         * the one that the user have viewed moments ago and isn't currently displayed
         * in the screen.
         **/
        private static final boolean ACCESS_ORDER = true;

        public DrawableCache() {
            super(INITIAL_CAPACITY, LOAD_FACTOR, ACCESS_ORDER);
        }

        @Override
        protected boolean removeEldestEntry(Entry<String, GifDrawable> eldest) {
            return size() > CACHE_CAPACITY;
        }
    }
}