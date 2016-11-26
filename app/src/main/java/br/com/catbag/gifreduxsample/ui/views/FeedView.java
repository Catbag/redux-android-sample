package br.com.catbag.gifreduxsample.ui.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.ui.EndlessRecyclerScrollListener;
import pl.droidsonroids.gif.GifDrawable;
import trikita.anvil.Anvil;
import trikita.anvil.recyclerview.Recycler;

import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOADING;
import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOAD_FAILED;
import static br.com.catbag.gifreduxsample.models.Gif.Status.NOT_DOWNLOADED;
import static br.com.catbag.gifreduxsample.ui.views.GifsAdapter.gifsAdapter;
import static trikita.anvil.BaseDSL.v;

/**
 * Created by niltonvasques on 10/26/16.
 */
public class FeedView extends ReactiveView {

    private Map<String, Gif> mGifs;
    private DrawableCache mDrawables = new DrawableCache();
    private LinearLayoutManager mLayoutManager;
    private GifsAdapter mGifsAdapter;
    private boolean mHasMoreGifs;

    public FeedView(Context context) {
        super(context);
    }

    public FeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void view() {
        Recycler.view(() -> {
            if (mLayoutManager == null) {
                mLayoutManager = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false);
            }
            if (mGifsAdapter == null) {
                mGifsAdapter = gifsAdapter(mGifs, this::renderGifView);
            }

            Recycler.layoutManager(mLayoutManager);
            Recycler.adapter(mGifsAdapter);
            Recycler.hasFixedSize(true);
            setupEndlessScrolling();

            if (!mGifs.equals(mGifsAdapter.getGifs())) {
                mGifsAdapter.setGifs(mGifs);
                mGifsAdapter.notifyDataSetChanged();
            }
        });

        onAnvilRendered();
    }

    private void setupEndlessScrolling() {
        //TODO: PR on Anvil Recycler to exposes addOnScrollListener
        ((RecyclerView) Anvil.currentView())
                .addOnScrollListener(new EndlessRecyclerScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        if (mHasMoreGifs) {
                            GifListActionCreator.getInstance().fetchGifs();
                        }
                    }
                });
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        return !newState.getGifs().equals(mGifs)
                || newState.getHasMoreGifs() != oldState.getHasMoreGifs();
    }

    @Override
    public void onStateChanged(AppState appState) {
        mGifs = appState.getGifs();
        mHasMoreGifs = appState.getHasMoreGifs();
    }

    @Override
    protected void initialState() {
        super.initialState();
        mGifs = MyApp.getFluxxan().getState().getGifs();
        mHasMoreGifs = MyApp.getFluxxan().getState().getHasMoreGifs();
    }

    private void renderGifView(Gif gif) {
        v(GifView.class, () -> ((GifView) Anvil.currentView())
                .withGifDrawable(createDrawable(gif))
                .withGif(gif));
        onAnvilRendered();
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