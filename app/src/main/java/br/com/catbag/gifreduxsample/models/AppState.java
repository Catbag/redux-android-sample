package br.com.catbag.gifreduxsample.models;

import org.immutables.value.Value;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by niltonvasques on 10/12/16.
 */

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
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

}
