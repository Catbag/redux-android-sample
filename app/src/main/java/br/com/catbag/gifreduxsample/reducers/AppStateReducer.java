package br.com.catbag.gifreduxsample.reducers;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;

import java.util.List;
import java.util.Map;

import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;

import static br.com.catbag.gifreduxsample.helpers.AppStateHelper.getGifStateByUuid;
import static br.com.catbag.gifreduxsample.helpers.AppStateHelper.gifListToMap;

/**
 * Created by raul@catbag.com.br on 10/26/16.
 */

public class AppStateReducer extends BaseAnnotatedReducer<AppState> {

    @BindAction(GifListActionCreator.GIF_LIST_LOADED)
    public AppState listLoaded(AppState state, List<Gif> gifs) {
        return createImmutableAppBuilder(state)
                .putAllGifs(gifListToMap(gifs))
                .build();
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_START)
    public AppState downloadStart(AppState state, String uuid) {
        return reduceGifState(state, GifInnerReducer.downloadStart(getGifStateByUuid(uuid, state)));
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_SUCCESS)
    public AppState downloadSuccess(AppState state, Map<String, Object> params) {
        return reduceGifState(state,
                GifInnerReducer.downloadSuccess(getGifState(params, state), params));
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_FAILURE)
    public AppState downloadFailure(AppState state, String uuid) {
        return reduceGifState(state,
                GifInnerReducer.downloadFailure(getGifStateByUuid(uuid, state)));
    }

    @BindAction(GifActionCreator.GIF_PLAY)
    public AppState play(AppState state, String uuid) {
        return reduceGifState(state, GifInnerReducer.play(getGifStateByUuid(uuid, state)));
    }

    @BindAction(GifActionCreator.GIF_PAUSE)
    public AppState pause(AppState state, String uuid) {
        return reduceGifState(state, GifInnerReducer.pause(getGifStateByUuid(uuid, state)));
    }

    //Business Logic
    private AppState reduceGifState(AppState state, Gif gif) {
        return createImmutableAppBuilder(state)
                .putGifs(gif.getUuid(), gif)
                .build();
    }

    //Helpers
    private static Gif getGifState(Map<String, Object> params, AppState state) {
        return getGifStateByUuid((String) params.get(PayloadParams.PARAM_UUID), state);
    }

    private static  ImmutableAppState.Builder createImmutableAppBuilder(AppState state) {
        return ImmutableAppState.builder().from(state);
    }
}
