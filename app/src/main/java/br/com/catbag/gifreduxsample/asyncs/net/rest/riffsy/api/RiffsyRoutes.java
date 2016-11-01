package br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.api;

import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Riffsy Api Service for getting "trending" and "search" api results.
 * <p>
 * Custom Api interfaces for the Riffsy Api.
 *
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
public interface RiffsyRoutes {
  /**
   * URL for Riffsy.
   */
  String BASE_URL = "https://api.riffsy.com/";

  /**
   * Riffsy public API key.
   */
  String API_KEY = "LIVDSRZULELA";

  /**
   * Riffsy limit results.
   */
  int DEFAULT_LIMIT_COUNT = 24;

  /**
   * Get trending gif results.
   * <p>
   * URL: https://api.riffsy.com/
   * Path: /v1/trending
   * Query: limit
   * Query: key
   * Query: pos
   * eg. https://api.riffsy.com/v1/trending?key=LIVDSRZULELA&limit=10&pos=1
   *
   * @param limit Limit results.
   * @param pos   Position of results.
   * @return Response of trending results.
   */
  @GET("/v1/trending?key=" + API_KEY)
  Call<RiffsyResponse> getTrendingResults(@Query("limit") int limit,
                                                @Query("pos") Float pos);
}
