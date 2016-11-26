package br.com.catbag.gifreduxsample.reducers.models;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import shared.TestHelper;

import static junit.framework.Assert.assertEquals;

/**
 Copyright 26/10/2016
 Felipe Piñeiro (fpbitencourt@gmail.com),
 Nilton Vasques (nilton.vasques@gmail.com) and
 Raul Abreu (raulccabreu@gmail.com)

 Be free to ask for help, email us!

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/

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
        return "{\"gifs\":{\"1\":{\"uuid\":\"1\",\"path\":\"\",\"url\":\"https://media.giphy.com/"
                + "media/l0HlE56oAxpngfnWM/giphy.gif\",\"title\":\"Gif 1\",\"watched\":false,"
                + "\"status\":\"NOT_DOWNLOADED\"},\"2\":{\"uuid\":\"2\",\"path\":\"\","
                + "\"url\":\"http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/"
                + "b3368a682fc5ff891e41baad2731f4b6.gif\",\"title\":\"Gif 2\",\"watched\":false,"
                + "\"status\":\"DOWNLOADED\"},\"3\":{\"uuid\":\"3\",\"path\":\"\",\"url\":"
                + "\"https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif\",\"title\":"
                + "\"Gif 3\",\"watched\":false,\"status\":\"DOWNLOAD_FAILED\"},\"4\":{\"uuid\":"
                + "\"4\",\"path\":\"\",\"url\":\"https://media.giphy.com/media/l2YWl1oQlNvthGWrK/"
                + "giphy.gif\",\"title\":\"Gif 4\",\"watched\":false,\"status\":\"LOOPING\"},"
                + "\"5\":{\"uuid\":\"5\",\"path\":\"\",\"url\":\"https://media.giphy.com/media/"
                + "3oriNQHSU0bVcFW5sA/giphy.gif\",\"title\":\"Gif 5\",\"watched\":false,"
                + "\"status\":\"PAUSED\"}},\"hasMoreGifs\":true}";
    }

    private AppState getDefaultAppState() {
        return TestHelper.buildAppState(buildGifMap());
    }

    private Map<String, Gif> buildGifMap() {
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

        Map<String, Gif> gifs = new LinkedHashMap<>();
        for (int i = 0; i < titles.length; i++) {
            Gif gif = ImmutableGif.builder()
                    .uuid(uuids[i])
                    .title(titles[i])
                    .url(urls[i])
                    .status(statuses[i])
                    .build();
            gifs.put(gif.getUuid(), gif);
        }

        return gifs;
    }
}
