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
        if (gif.getStatus() != Gif.Status.NOT_DOWNLOADED) return gif;
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.DOWNLOADING)
                .build();
    }

    public static Gif downloadSuccess(Gif gif, Map<String, Object> params) {
        if (gif.getStatus() != Gif.Status.DOWNLOADING) return gif;
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.DOWNLOADED)
                .path((String) params.get(PayloadParams.PARAM_PATH))
                .build();
    }

    public static Gif downloadFailure(Gif gif) {
        if (gif.getStatus() != Gif.Status.DOWNLOADING) return gif;
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.DOWNLOAD_FAILED)
                .build();
    }

    public static Gif play(Gif gif) {
        if (gif.getStatus() != Gif.Status.DOWNLOADED && gif.getStatus() != Gif.Status.PAUSED)
            return gif;
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.LOOPING)
                .watched(true)
                .build();
    }

    public static Gif pause(Gif gif) {
        if (gif.getStatus() != Gif.Status.LOOPING) return gif;
        return createImmutableGifBuilder(gif)
                .status(Gif.Status.PAUSED)
                .build();
    }

    //Helpers
    private static  ImmutableGif.Builder createImmutableGifBuilder(Gif gif) {
        return ImmutableGif.builder().from(gif);
    }
}
