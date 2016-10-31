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

    public static Gif downloadStart(Gif gif) {
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.DOWNLOADING)
                .build();
    }

    public static Gif downloadSuccess(Gif gif, Map<String, Object> params) {
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.DOWNLOADED)
                .path((String) params.get(PayloadParams.PARAM_PATH))
                .build();
    }

    public static Gif downloadFailure(Gif gif) {
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.DOWNLOAD_FAILED)
                .build();
    }

    public static Gif play(Gif gif) {
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.LOOPING)
                .watched(true)
                .build();
    }

    public static Gif pause(Gif gif) {
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.PAUSED)
                .build();
    }

    //Helpers
    private static  ImmutableGif.Builder createImmutableGifBuilder(Gif gif) {
        return ImmutableGif.builder().from(gif);
    }
}
