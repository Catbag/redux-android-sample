package br.com.catbag.gifreduxsample.models;

/**
 * Created by felipe on 20/10/16.
 */

import org.immutables.value.Value;

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
public abstract class Gif {

    public enum Status {
        PAUSED, LOOPING, DOWNLOADING, DOWNLOADED, NOT_DOWNLOADED, DOWNLOAD_FAILED
    }

    @Value
    public abstract String getUuid();

    @Value.Default
    public String getPath() {
        return "";
    }

    @Value
    public abstract String getUrl();

    @Value
    public abstract String getTitle();

    @Value.Default
    public boolean getWatched() {
        return false;
    }

    @Value.Default
    public Status getStatus() {
        return Status.NOT_DOWNLOADED;
    }

}
