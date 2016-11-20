package br.com.catbag.gifreduxsample.reducers;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;

/**
 * Created by raul@catbag.com.br on 10/26/16.
 */

public class AppStateReducer extends BaseAnnotatedReducer<AppState> {

    @BindAction(GifListActionCreator.GIF_LIST_UPDATED)
    public AppState listUpdated(AppState state, Map<String, Object> params) {
        Map<String, Gif> gifs = (Map<String, Gif>) params.get(PayloadParams.PARAM_GIFS);
        boolean hasMore = (boolean) params.get(PayloadParams.PARAM_HAS_MORE);
        return createImmutableAppBuilder(state)
                .putAllGifs(gifs)
                .hasMoreGifs(hasMore)
                .build();
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_START)
    public AppState downloadStart(AppState state, String uuid) {
        return reduceGifState(state, GifInnerReducer.downloadStart(state.getGifs().get(uuid)));
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_SUCCESS)
    public AppState downloadSuccess(AppState state, Map<String, Object> params) {
        return reduceGifState(state,
                GifInnerReducer.downloadSuccess(getGifState(params, state), params));
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_FAILURE)
    public AppState downloadFailure(AppState state, String uuid) {
        return reduceGifState(state,
                GifInnerReducer.downloadFailure(state.getGifs().get(uuid)));
    }

    @BindAction(GifActionCreator.GIF_PLAY)
    public AppState play(AppState state, String uuid) {
        return reduceGifState(state, GifInnerReducer.play(state.getGifs().get(uuid)));
    }

    @BindAction(GifActionCreator.GIF_PAUSE)
    public AppState pause(AppState state, String uuid) {
        return reduceGifState(state, GifInnerReducer.pause(state.getGifs().get(uuid)));
    }

    //Business Logic
    private AppState reduceGifState(AppState state, Gif gif) {
        Map<String, Gif> gifs = new LinkedHashMap<>(state.getGifs());
        gifs.put(gif.getUuid(), gif);
        return createImmutableAppBuilder(state)
                .gifs(gifs)
                .build();
    }

    //Helpers
    private static Gif getGifState(Map<String, Object> params, AppState state) {
        return state.getGifs().get(params.get(PayloadParams.PARAM_UUID));
    }

    private static  ImmutableAppState.Builder createImmutableAppBuilder(AppState state) {
        return ImmutableAppState.builder().from(state);
    }
}
