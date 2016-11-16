package br.com.catbag.gifreduxsample.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import org.immutables.value.Value;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by niltonvasques on 10/12/16.
 */

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
@JsonSerialize(as = ImmutableAppState.class)
@JsonDeserialize(as = ImmutableAppState.class)
public abstract class AppState {

    @Value.Default
    @Value.NaturalOrder
    public SortedMap<String, Gif> getGifs() {
        return new TreeMap<>();
    }

    @Value.Default
    public boolean getHasMoreGifs() {
        return true;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static AppState fromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new GuavaModule());
        return mapper.readValue(json, AppState.class);
    }

}