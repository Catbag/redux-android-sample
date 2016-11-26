package br.com.catbag.gifreduxsample.reducers;

import java.util.Map;

import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

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

final class GifInnerReducer {
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
