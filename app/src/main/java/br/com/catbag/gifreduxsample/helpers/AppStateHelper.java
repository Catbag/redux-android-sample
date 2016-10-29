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

    public static Gif getGifStateByUuid(String uuid, AppState state) {
        return state.getGifs().get(uuid);
    }

    public static List<Gif> extractGifList(AppState state) {
        return new ArrayList<>(state.getGifs().values());
    }

    public static Map<String, Gif> gifListToMap(List<Gif> gifs) {
        Map<String, Gif> map = new HashMap<>();
        for (Gif gif : gifs) map.put(gif.getUuid(), gif);
        return map;
    }
}
