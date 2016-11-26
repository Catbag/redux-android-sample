package br.com.catbag.gifreduxsample.ui.views;

import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.catbag.gifreduxsample.models.Gif;
import trikita.anvil.RenderableRecyclerViewAdapter;

/**
 Copyright 26/10/2016
 Felipe Piñeiro (fpbitencourt@gmail.com),
 Nilton Vasques (nilton.vasques@gmail.com) and
 Raul Abreu (raulccabreu@gmail.com)

 Be free to ask for help, email us!

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/

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

