package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.api;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.retrofit.RetrofitBuilder;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.api.RiffsyRoutes;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyResponse;
import br.com.catbag.gifreduxsample.reducers.mocks.ServerMockTestBase;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.DELTA;

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
