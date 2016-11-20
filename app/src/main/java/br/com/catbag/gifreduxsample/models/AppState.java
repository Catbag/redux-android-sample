package br.com.catbag.gifreduxsample.models;

import org.immutables.value.Value;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by niltonvasques on 10/12/16.
 */

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
public abstract class AppState {

    @Value.Default
    public Map<String, Gif> getGifs() {
        return new LinkedHashMap<>();
    }

    @Value.Default
    public boolean getHasMoreGifs() {
        return true;
    }

}
