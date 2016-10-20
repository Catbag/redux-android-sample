package br.com.catbag.gifreduxsample.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;

/**
 * Created by felipe on 21/10/16.
 */

public final class AppStateHelper {

    private AppStateHelper() { }

    public static Gif getFirstGif(AppState state) {
        if (state.getGifs().size() <= 0) return null;
        return toGifList(state.getGifs()).get(0);
    }

    public static Gif getLastGif(AppState state) {
        if (state.getGifs().size() <= 0) return null;
        return toGifList(state.getGifs()).get(state.getGifs().size());
    }

    public static Gif getGifStateByUuid(String uuid, AppState state) {
        return state.getGifs().get(uuid);
    }

    private static List<Gif> toGifList(Map map) {
        return new ArrayList<>(map.values());
    }

    public static List<Gif> getListGifs(AppState state) {
        return toGifList(state.getGifs());
    }

    public static Map<String, Gif> toMap(List<Gif> gifs) {
        Map<String, Gif> map = new HashMap<>();
        for (Gif gif : gifs) map.put(gif.getUuid(), gif);
        return map;
    }
}
