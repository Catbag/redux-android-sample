package br.com.catbag.gifreduxsample.reducers.models;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import shared.TestHelper;

import static junit.framework.Assert.assertEquals;

/**
 * Created by felipe on 13/11/16.
 */

public class AppStateTest {

    @Test
    public void testAppStateToJson() throws JsonProcessingException {
        assertEquals(getDefaultAppState().toJson(), getDefaultJSON());
    }

    @Test
    public void testJsonToAppState() throws IOException {
        assertEquals(AppState.fromJson(getDefaultJSON()), getDefaultAppState());
    }

    private String getDefaultJSON() {
        return "{\"gifs\":{\"1\":{\"uuid\":\"1\",\"path\":\"\",\"url\":\"https://media.giphy" +
                ".com/media/l0HlE56oAxpngfnWM/giphy.gif\",\"title\":\"Gif 1\",\"watched\":false," +
                "\"status\":\"NOT_DOWNLOADED\"},\"2\":{\"uuid\":\"2\",\"path\":\"\"," +
                "\"url\":\"http://inspirandoideias.com" +
                ".br/blog/wp-content/uploads/2015/03/b3368a682fc5ff891e41baad2731f4b6.gif\"," +
                "\"title\":\"Gif 2\",\"watched\":false,\"status\":\"DOWNLOADED\"}," +
                "\"3\":{\"uuid\":\"3\",\"path\":\"\",\"url\":\"https://media.giphy" +
                ".com/media/9fbYYzdf6BbQA/giphy.gif\",\"title\":\"Gif 3\",\"watched\":false," +
                "\"status\":\"DOWNLOAD_FAILED\"},\"4\":{\"uuid\":\"4\",\"path\":\"\"," +
                "\"url\":\"https://media.giphy.com/media/l2YWl1oQlNvthGWrK/giphy.gif\"," +
                "\"title\":\"Gif 4\",\"watched\":false,\"status\":\"LOOPING\"}," +
                "\"5\":{\"uuid\":\"5\",\"path\":\"\",\"url\":\"https://media.giphy" +
                ".com/media/3oriNQHSU0bVcFW5sA/giphy.gif\",\"title\":\"Gif 5\",\"watched\":false," +
                "\"status\":\"PAUSED\"}},\"hasMoreGifs\":true}";
    }

    private AppState getDefaultAppState() {
        return TestHelper.buildAppState(buildGifList());
    }

    private List<Gif> buildGifList() {
        String[] uuids = {"1", "2", "3", "4", "5"};
        String[] titles = {"Gif 1", "Gif 2", "Gif 3", "Gif 4", "Gif 5"};
        String[] urls = {
                "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif",
                "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/"
                        + "b3368a682fc5ff891e41baad2731f4b6.gif",
                "https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif",
                "https://media.giphy.com/media/l2YWl1oQlNvthGWrK/giphy.gif",
                "https://media.giphy.com/media/3oriNQHSU0bVcFW5sA/giphy.gif"
        };
        Gif.Status[] statuses = {Gif.Status.NOT_DOWNLOADED, Gif.Status.DOWNLOADED,
                Gif.Status.DOWNLOAD_FAILED, Gif.Status.LOOPING, Gif.Status.PAUSED};

        List<Gif> gifs = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Gif gif = ImmutableGif.builder()
                    .uuid(uuids[i])
                    .title(titles[i])
                    .url(urls[i])
                    .status(statuses[i])
                    .build();
            gifs.add(gif);
        }

        return gifs;
    }
}
