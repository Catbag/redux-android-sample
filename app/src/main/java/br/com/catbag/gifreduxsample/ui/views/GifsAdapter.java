package br.com.catbag.gifreduxsample.ui.views;

import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.catbag.gifreduxsample.models.Gif;
import trikita.anvil.RenderableRecyclerViewAdapter;

/**
 * Created by raulcca on 11/18/16.
 */

public final class GifsAdapter extends RenderableRecyclerViewAdapter {
    private GifsAdapterRenderable mRenderable;
    private Map<String, Gif> mGifs;
    private List mUuids;

    private GifsAdapter(Map<String, Gif> gifs, GifsAdapterRenderable renderable) {
        setGifs(gifs);
        mRenderable = renderable;
    }

    public static GifsAdapter gifsAdapter(Map<String, Gif> gifs, GifsAdapterRenderable renderable) {
        GifsAdapter adapter = new GifsAdapter(gifs, renderable);
        adapter.setHasStableIds(false);
        return adapter;
    }

    public Map getGifs() {
        return mGifs;
    }

    public void setGifs(Map<String, Gif> gifs) {
        mGifs = gifs;
        mUuids = Arrays.asList(mGifs.keySet().toArray());
    }

    public int getItemCount() {
        return mGifs.size();
    }

    public long getItemId(int pos) {
        return pos;
    }

    public int getItemViewType(int pos) {
        Object item = mGifs.get(mUuids.get(pos));
        return item == null ? 0 : item.getClass().hashCode();
    }

    public void view(RecyclerView.ViewHolder holder) {
        mRenderable.view(mGifs.get(mUuids.get(holder.getAdapterPosition())));
    }

    public interface GifsAdapterRenderable {
        void view(Gif gif);
    }
}

