package br.com.catbag.giffluxsample.reducers;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;

import java.util.UUID;

import br.com.catbag.giffluxsample.actions.GifActionCreator;
import br.com.catbag.giffluxsample.models.AppState;
import br.com.catbag.giffluxsample.models.ImmutableAppState;

/**
 * Created by niltonvasques on 10/12/16.
 */

public class GifReducer extends BaseAnnotatedReducer<AppState> {

    @BindAction(GifActionCreator.PLAY_GIF)
    public AppState play(AppState state, Object ...args) {
        return ImmutableAppState.builder()
                .from(state)
                .gifStatus(AppState.GifStatus.LOOPING)
                .gifWatched(true)
                .build();
    }

    @BindAction(GifActionCreator.PAUSE_GIF)
    public AppState pause(AppState state, Object ...args) {
        return ImmutableAppState.builder()
                .from(state)
                .gifStatus(AppState.GifStatus.PAUSED)
                .build();
    }
}
