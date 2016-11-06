package br.com.catbag.gifreduxsample.models;

/**
 * Created by felipe on 20/10/16.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.io.IOException;

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
@JsonSerialize(as = ImmutableGif.class)
@JsonDeserialize(as = ImmutableGif.class)
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

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static Gif fromJson(String json) throws IOException {
        return new ObjectMapper().readValue(json, Gif.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Gif))
            return false;
        if (obj == this)
            return true;

        Gif gif = (Gif) obj;
        return this.getUuid().equals(gif.getUuid())
                && this.getUuid().equals(gif.getUuid())
                && this.getTitle().equals(gif.getTitle())
                && this.getStatus().equals(gif.getStatus())
                && this.getUrl().equals(gif.getUrl())
                && this.getPath().equals(gif.getPath())
                && this.getWatched() == gif.getWatched();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}