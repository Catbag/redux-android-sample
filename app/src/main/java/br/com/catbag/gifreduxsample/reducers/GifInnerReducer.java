package br.com.catbag.gifreduxsample.reducers;

import java.util.Map;

import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

/**
 * Created by niltonvasques on 10/12/16.
 */

public final class GifInnerReducer {
    private GifInnerReducer() {
    }

    public static Gif downloadStart(Gif state) {
        return createImmutableGifBuilder(state)
                .status(Gif.Status.DOWNLOADING)
                .build();
    }

    public static Gif downloadSuccess(Gif state, Map<String, Object> params) {
        return createImmutableGifBuilder(state)
                .status(Gif.Status.DOWNLOADED)
                .path((String) params.get(PayloadParams.PARAM_PATH))
                .build();
    }

    public static Gif downloadFailure(Gif state, Map<String, Object> params) {
        return createImmutableGifBuilder(state)
                .status(Gif.Status.NOT_DOWNLOADED)
                .downloadFailureMsg((String) params.get(PayloadParams.PARAM_DOWNLOAD_FAILURE_MSG))
                .build();
    }

    public static Gif play(Gif state) {
        return createImmutableGifBuilder(state)
                .status(Gif.Status.LOOPING)
                .watched(true)
                .build();
    }

    public static Gif pause(Gif state) {
        return createImmutableGifBuilder(state)
                .status(Gif.Status.PAUSED)
                .build();
    }

    //Helpers
    private static  ImmutableGif.Builder createImmutableGifBuilder(Gif state) {
        return ImmutableGif.builder().from(state);
    }
}
