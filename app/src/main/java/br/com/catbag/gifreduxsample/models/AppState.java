package br.com.catbag.gifreduxsample.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import org.immutables.value.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by niltonvasques on 10/12/16.
 */

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
@JsonSerialize(as = ImmutableAppState.class)
@JsonDeserialize(as = ImmutableAppState.class)
public abstract class AppState {

    @Value.Default
    public Map<String, Gif> getGifs() {
        return new HashMap<>();
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AppState))
            return false;
        if (obj == this)
            return true;

        AppState state = (AppState) obj;
        if (state.getGifs() == null)
            return false;
        if (this.getGifs().size() != state.getGifs().size())
            return false;

        for (String key : this.getGifs().keySet()) {
            if (!state.getGifs().containsKey(key)) return false;
            if (!state.getGifs().get(key).equals(this.getGifs().get(key))) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}