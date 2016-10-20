package br.com.catbag.gifreduxsample.ui.components;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.catbag.gifreduxsample.models.Gif;
import pl.droidsonroids.gif.GifDrawable;
import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;
import trikita.anvil.recyclerview.Recycler;

import static br.com.catbag.gifreduxsample.models.Gif.Status.DOWNLOADING;
import static br.com.catbag.gifreduxsample.models.Gif.Status.NOT_DOWNLOADED;
import static trikita.anvil.BaseDSL.v;

/**
 * Created by niltonvasques on 10/26/16.
 */
public class FeedComponent extends RenderableView {

    private List<Gif> mGifs = new ArrayList<>();
    private Map<String, GifDrawable> mDrawables = new HashMap<>();
    private LinearLayoutManager mLayoutManager;

    public FeedComponent(Context context) {
        super(context);
    }

    public FeedComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                Gif gif = mGifs.get(viewHolder.getAdapterPosition());
                renderGifView(gif);
            }));
        });
    }

    public void setGifs(List<Gif> gifs) {
        mGifs = gifs;
    }

    private void renderGifView(final Gif gif) {
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
            } else if (gif.getStatus() != NOT_DOWNLOADED && gif.getStatus() != DOWNLOADING) {
                drawable = new GifDrawable(gif.getPath());
                mDrawables.put(gif.getUuid(), drawable);
            }
        } catch (IOException e) {
            Log.e("Feed", gif.toString(), e);
        }
        return drawable;
    }
}
