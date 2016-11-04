package shared;

import com.umaplay.fluxxan.Fluxxan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.helpers.AppStateHelper;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

/**
 * Created by niltonvasques on 10/31/16.
 */

public final class TestUtils {
    public static final String DEFAULT_UUID = "default-uuid";

    private TestUtils() { }

    public static Fluxxan<AppState> getFluxxan() {
        return MyApp.getFluxxan();
    }

    public static AppState createStateFromGif(Gif gif) {
        return ImmutableAppState.builder().putGifs(gif.getUuid(), gif).build();
    }

    public static AppState createStateFromGifs(List<Gif> gifs) {
        return ImmutableAppState.builder().putAllGifs(AppStateHelper.gifListToMap(gifs)).build();
    }

    public static ImmutableGif.Builder gifBuilder() {
        return ImmutableGif.builder().uuid(UUID.randomUUID().toString())
                .title("")
                .url("");
    }

    public static List<Gif> getFakeGifs() {
        String[] titles = {"Gif 1", "Gif 2", "Gif 3", "Gif 4", "Gif 5" };
        String[] urls = {
                "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif",
                "http://inspirandoideias.com.br/blog/wp-content/uploads/2015/03/"
                        + "b3368a682fc5ff891e41baad2731f4b6.gif",
                "https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif",
                "https://media.giphy.com/media/l2YWl1oQlNvthGWrK/giphy.gif",
                "https://media.giphy.com/media/3oriNQHSU0bVcFW5sA/giphy.gif"
        };

        List<Gif> gifs = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Gif gif = ImmutableGif.builder().uuid(UUID.randomUUID().toString())
                    .title(titles[i])
                    .url(urls[i])
                    .build();
            gifs.add(gif);
        }

        return gifs;
    }

    public static Gif buildGif(Gif.Status status) {
        return buildGif(status, DEFAULT_UUID);
    }

    public static Gif buildGif(Gif.Status status, String uuid) {
        return createDefaultImmutableGif()
                .uuid(uuid)
                .title("Gif")
                .url( "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif")
                .status(status)
                .build();
    }

    public static ImmutableGif.Builder createDefaultImmutableGif(){
        return ImmutableGif.builder().uuid(DEFAULT_UUID)
                .title("Gif")
                .url( "https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif")
                .status(Gif.Status.NOT_DOWNLOADED);

    }

    public static Gif getFirstGif() {
        AppState state = getFluxxan().getState();
        if (state.getGifs().size() <= 0) return null;
        return (Gif) state.getGifs().values().toArray()[0];
    }

    public static InputStream getResourceAsStream(String fileName) {
        return TestUtils.class.getResourceAsStream(fileName);
    }
}
