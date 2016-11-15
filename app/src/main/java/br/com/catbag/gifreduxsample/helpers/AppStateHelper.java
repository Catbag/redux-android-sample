package br.com.catbag.gifreduxsample.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;

/**
 * Created by felipe on 21/10/16.
 */

public final class AppStateHelper {

    private AppStateHelper() { }

    public static Gif getGifStateByUuid(String uuid, AppState state) {
        return state.getGifs().get(uuid);
    }

    public static List<Gif> extractGifList(AppState state) {
        return new ArrayList<>(state.getGifs().values());
    }

    public static SortedMap<String, Gif> gifListToMap(List<Gif> gifs) {
        SortedMap<String, Gif> map = new TreeMap<>();
        for (Gif gif : gifs) map.put(gif.getUuid(), gif);
        return map;
    }
}
