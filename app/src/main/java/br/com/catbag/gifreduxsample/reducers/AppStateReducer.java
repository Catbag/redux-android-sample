package br.com.catbag.gifreduxsample.reducers;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.catbag.gifreduxsample.actions.AppStateActionCreator;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;

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

public class AppStateReducer extends BaseAnnotatedReducer<AppState> {

    @BindAction(AppStateActionCreator.APP_STATE_LOADED)
    public AppState stateLoaded(AppState oldState, AppState newState) {
        return createImmutableAppBuilder(newState)
                .build();
    }

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