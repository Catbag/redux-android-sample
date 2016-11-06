package br.com.catbag.gifreduxsample.asyncs.data.storage;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

import static br.com.catbag.gifreduxsample.helpers.AppStateHelper.gifListToMap;

/**
 * Created by felipe on 26/10/16.
 */

public class Database {

    private static final String TAG_APP_STATE = "TAG_APP_STATE";
    private Context mContext;

    public Database(final Context context) {
        mContext = context;
    }

    public AppState getAppState() throws SnappydbException, IOException {
        DB db = DBFactory.open(mContext);
        AppState appState = AppState.fromJson(db.get(TAG_APP_STATE));
        db.close();
        return appState;
    }

    public void saveAppState(AppState appState) throws SnappydbException, JsonProcessingException {
        DB db = DBFactory.open(mContext);
        db.put(TAG_APP_STATE, appState.toJson());
        db.close();
    }

    //TODO: só carregar no modo debug
    public AppState seed() {
        List<Gif> gifs = new ArrayList<>();

        String[] titles = {"Gif 1", "Gif 2", "Gif 3", "Gif 4", "Gif 5" };
        String[] urls = {
                "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif",
                "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/"
                        + "b3368a682fc5ff891e41baad2731f4b6.gif",
                "https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif",
                "https://media.giphy.com/media/l2YWl1oQlNvthGWrK/giphy.gif",
                "https://media.giphy.com/media/3oriNQHSU0bVcFW5sA/giphy.gif"
        };

        for (int i = 0; i < titles.length; i++) {
            Gif gif = ImmutableGif.builder()
                    .uuid(UUID.randomUUID().toString())
                    .title(titles[i])
                    .url(urls[i])
                    .build();
            gifs.add(gif);
        }

        AppState appState = ImmutableAppState.builder()
                .putAllGifs(gifListToMap(gifs))
                .build();

        try {
            saveAppState(appState);
        } catch (SnappydbException | JsonProcessingException e) {
            Log.e(getClass().getSimpleName(), "unsaved appstate seed", e);
        }

        return appState;
    }
}