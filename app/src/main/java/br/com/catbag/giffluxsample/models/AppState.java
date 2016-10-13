package br.com.catbag.giffluxsample.models;

import org.immutables.value.Value;

/**
 * Created by niltonvasques on 10/12/16.
 */

@Value.Immutable
public abstract class AppState {

    @Value.Default
    public String getUrl() {
       return "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/" +
               "b3368a682fc5ff891e41baad2731f4b6.gif";
    }

    @Value.Default
    public String getGifTitle(){
        return "title bolado";
    }

    @Value.Default
    public boolean getGifWatched(){
        return false;
    }

    @Value.Default
    public GifStatus getGifStatus(){
       return GifStatus.NOT_LOADED;
    }

    public enum GifStatus {
       PAUSED, LOOPING, NOT_LOADED
    }

}