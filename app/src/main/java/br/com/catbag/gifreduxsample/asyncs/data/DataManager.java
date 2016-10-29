package br.com.catbag.gifreduxsample.asyncs.data;

import java.util.List;

import br.com.catbag.gifreduxsample.asyncs.data.storage.Database;
import br.com.catbag.gifreduxsample.models.Gif;

/**
 * Created by felipe on 26/10/16.
 */

public class DataManager {
    private Database mDatabase = new Database();

    public void getAllGifs(final GifListLoadListener listener) {
        new Thread(() -> {
            listener.onLoaded(mDatabase.getAllGifs());
        }).start();
    }

    public interface GifListLoadListener {
        void onLoaded(List<Gif> gifs);
    }

}
