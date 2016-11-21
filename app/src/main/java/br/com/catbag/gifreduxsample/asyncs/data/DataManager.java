package br.com.catbag.gifreduxsample.asyncs.data;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.catbag.gifreduxsample.asyncs.data.storage.Database;
import br.com.catbag.gifreduxsample.asyncs.net.rest.retrofit.RetrofitBuilder;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.api.RiffsyRoutes;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyMedia;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResponse;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResult;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by felipe on 26/10/16.
 */

public class DataManager {
    private static Float sRiffsyNext = null;
    private final RiffsyRoutes mRiffsyRoutes;

    private Database mDatabase = new Database();

    public DataManager() {
        mRiffsyRoutes = RetrofitBuilder.getInstance()
                .createApiEndpoint(RiffsyRoutes.class, RiffsyRoutes.BASE_URL);
    }

    public void fetchGifs(final GifListLoadListener listener) {
        Call call = mRiffsyRoutes.getTrendingResults(RiffsyRoutes.DEFAULT_LIMIT_COUNT, sRiffsyNext);
        call.enqueue(new Callback<RiffsyResponse>() {
            @Override
            public void onResponse(Call<RiffsyResponse> call, Response<RiffsyResponse> response) {
                Map<String, Gif> gifs = extractGifsFromRiffsyResponse(response);

                sRiffsyNext = response.body().next();
                boolean hasMore = sRiffsyNext != null;
                listener.onLoaded(gifs, hasMore);
            }

            @Override
            public void onFailure(Call<RiffsyResponse> call, Throwable t) {
                Log.e(getClass().getSimpleName(), "onFailure", t);
            }
        });
    }

    @NonNull
    private Map<String, Gif> extractGifsFromRiffsyResponse(Response<RiffsyResponse> response) {
        List<RiffsyResult> results = response.body().results();
        Map<String, Gif> gifs = new LinkedHashMap<>();
        for (RiffsyResult result : results) {
            List<RiffsyMedia> riffsyMedias = result.media();
            if (riffsyMedias.size() > 0) {
                Gif gif = ImmutableGif.builder()
                        .url(riffsyMedias.get(0).gif().url())
                        .title(result.title())
                        .uuid(UUID.randomUUID().toString()).build();
                gifs.put(gif.getUuid(), gif);
            }
        }
        return gifs;
    }

    public interface GifListLoadListener {
        void onLoaded(Map<String, Gif> gifs, boolean hasMore);
    }

}
