package br.com.catbag.gifreduxsample.asyncs.data.storage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

/**
 * Created by felipe on 26/10/16.
 */

public class Database {

    public Map<String, Gif> getAllGifs() {
        Map<String, Gif> gifs = new LinkedHashMap<>();

        String[] titles = {"Gif 1", "Gif 2", "Gif 3", "Gif 4", "Gif 5" };
        String[] urls = {
                "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif",
                "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/"
                        + "b3368a682fc5ff891e41baad2731f4b6.gif",
                "https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif",
                "https://media.giphy.com/media/l2YWl1oQlNvthGWrK/giphy.gif",
                "https://media.giphy.com/media/3oriNQHSU0bVcFW5sA/giphy.gif"
        };

        for (int i = 0; i < titles.length; i++) {
            Gif gif = ImmutableGif.builder()
                    .uuid(UUID.randomUUID().toString())
                    .title(titles[i])
                    .url(urls[i])
                    .build();
            gifs.put(gif.getUuid(), gif);
        }
        return gifs;
    }

}
