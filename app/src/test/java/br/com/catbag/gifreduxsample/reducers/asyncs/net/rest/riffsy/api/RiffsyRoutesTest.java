package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.api;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.net.rest.retrofit.RetrofitBuilder;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.api.RiffsyRoutes;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResponse;
import br.com.catbag.gifreduxsample.reducers.mocks.ServerMockTestBase;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.DELTA;

/**
 * Created by niltonvasques on 11/2/16.
 */

public class RiffsyRoutesTest extends ServerMockTestBase {
    private RiffsyRoutes mSut;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mSut = RetrofitBuilder.getInstance().createApiEndpoint(RiffsyRoutes.class, mMockEndPoint);
    }

    @Test
    public void testTrendingResultsUrlShouldParseCorrectly() throws Exception {
        // Response
        sendMockMessages("/trending_results.json");

        // Request
        Response<RiffsyResponse> response = mSut
                .getTrendingResults(RiffsyRoutes.DEFAULT_LIMIT_COUNT, null)
                .execute();

        assertEquals(response.body().next(), Float.parseFloat("1474504590.85657"), DELTA);
        assertEquals(response.body().results().size(), 1);
        assertEquals(response.body().results().get(0).media().size(), 1);
        assertEquals(response.body().results().get(0).media().get(0).gif().url(),
                "https://media.riffsy.com/images/7d95a1f8a8750460a82b04451be26d69/raw");
        assertEquals(response.body().results().get(0).title(), "miss you");
    }
}
