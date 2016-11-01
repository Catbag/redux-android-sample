package br.com.catbag.gifreduxsample.asyncs.net.rest.retrofit;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by niltonvasques on 11/1/16.
 */

public final class RetrofitBuilder {
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
}
