package br.com.catbag.giffluxsample.reducers;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;

import br.com.catbag.giffluxsample.actions.GifActionCreator;
import br.com.catbag.giffluxsample.models.AppState;
import br.com.catbag.giffluxsample.models.ImmutableAppState;

/**
 * Created by niltonvasques on 10/12/16.
 */

public class GifReducer extends BaseAnnotatedReducer<AppState> {

    @BindAction(GifActionCreator.GIF_PLAY)
    public AppState play(AppState state, Object ...args) {
        return ImmutableAppState.builder()
                .from(state)
                .gifStatus(AppState.GifStatus.LOOPING)
                .gifWatched(true)
                .build();
    }

    @BindAction(GifActionCreator.GIF_PAUSE)
    public AppState pause(AppState state, Object ...args) {
        return ImmutableAppState.builder()
                .from(state)
                .gifStatus(AppState.GifStatus.PAUSED)
                .build();
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_SUCCESS)
    public AppState downloadSuccess(AppState state, String localPath) {
        return ImmutableAppState.builder()
                .from(state)
                .gifStatus(AppState.GifStatus.DOWNLOADED)
                .gifLocalPath(localPath)
                .build();
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_FAILURE)
    public AppState downloadFailure(AppState state, String errorMsg) {
        return ImmutableAppState.builder()
                .from(state)
                .gifStatus(AppState.GifStatus.NOT_DOWNLOADED)
                .gifDownloadFailureMsg(errorMsg)
                .build();
    }

    @BindAction(GifActionCreator.GIF_DOWNLOAD_STARTED)
    public AppState downloadStarted(AppState state, Object ...args) {
        return ImmutableAppState.builder()
                .from(state)
                .gifStatus(AppState.GifStatus.DOWNLOADING)
                .build();
    }
}
