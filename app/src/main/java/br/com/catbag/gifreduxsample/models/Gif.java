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

    //abstract method are immutable by default
    public abstract String getUuid();

    @Value.Default
    public String getPath() {
        return "";
    }

    public abstract String getUrl();
    
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

}