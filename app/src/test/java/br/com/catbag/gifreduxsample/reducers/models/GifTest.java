package br.com.catbag.gifreduxsample.reducers.models;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

import java.io.IOException;

import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

import static junit.framework.Assert.assertEquals;

/**
 * Created by felipe on 13/11/16.
 */

public class GifTest {

    @Test
    public void testGifToJson() throws JsonProcessingException {
        assertEquals(getDefaultGif().toJson(), getDefaultJSON());
    }

    @Test
    public void testJsonToAppState() throws IOException {
        assertEquals(Gif.fromJson(getDefaultJSON()), getDefaultGif());
    }

    private String getDefaultJSON() {
        return "{\"uuid\":\"1\",\"path\":\"\",\"url\":\"https://media.giphy.com/media/"
                + "l0HlE56oAxpngfnWM/giphy.gif\",\"title\":\"Gif Default\","
                + "\"watched\":false,\"status\":\"DOWNLOADED\"}";
    }

    private Gif getDefaultGif() {
        return ImmutableGif.builder()
                .uuid("1")
                .title("Gif Default")
                .url("https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif")
                .status(Gif.Status.DOWNLOADED)
                .build();
    }

}
