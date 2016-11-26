package br.com.catbag.gifreduxsample.asyncs.data.net.rest.retrofit;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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

public final class RetrofitBuilder {
    private static final String DEFAULT_URL = "http://none.com";
    private static RetrofitBuilder sInstance;
    private Executor mThreadPoolExecutor;

    private RetrofitBuilder() {
        createExecutor();
    }

    public static RetrofitBuilder getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitBuilder();
        }
        return sInstance;
    }

    public <T> T createApiEndpoint(Class<T> routes, String endpoint) {
        return buildRetrofit(endpoint).create(routes);
    }

    public DownloadRoute createDownloadEndpoint() {
        return buildRetrofit(DEFAULT_URL).create(DownloadRoute.class);
    }

    private void createExecutor() {
        int cpuCount = Runtime.getRuntime().availableProcessors();
        int corePoolSize = cpuCount + 1;
        mThreadPoolExecutor = Executors.newFixedThreadPool(corePoolSize);
    }

    @NonNull
    private Retrofit buildRetrofit(String endpoint) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(endpoint)
                .callbackExecutor(mThreadPoolExecutor)
                .client(new OkHttpClient()).build();
    }

    public interface DownloadRoute {
        @GET
        @Streaming
        Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
    }
}
