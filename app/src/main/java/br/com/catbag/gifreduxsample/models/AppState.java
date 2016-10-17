package br.com.catbag.gifreduxsample.models;

import org.immutables.value.Value;

/**
 * Created by niltonvasques on 10/12/16.
 */

@Value.Immutable
public abstract class AppState {

    @Value.Default
    public String getGifLocalPath() {
        return "";
    }

    @Value.Default
    public String getGifDownloadFailureMsg() {
        return "";
    }

    @Value.Default
    public String getGifUrl() {
        return "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/" +
                "b3368a682fc5ff891e41baad2731f4b6.gif";
    }

    @Value.Default
    public String getGifTitle() {
        return "goku";
    }

    @Value.Default
    public boolean getGifWatched() {
        return false;
    }

    @Value.Default
    public GifStatus getGifStatus() {
        return GifStatus.NOT_DOWNLOADED;
    }

    public enum GifStatus {
        PAUSED, LOOPING, DOWNLOADING, DOWNLOADED, NOT_DOWNLOADED
    }

}