package br.com.catbag.gifreduxsample.asyncs.restservice;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by felipe on 13/10/16.
 */

public final class RoutesGenerator {
    private static final String CATBAG_URL = "http://www.catbag.com.br";
    private static OkHttpClient.Builder sHttpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder sBuilder
            = new Retrofit.Builder()
                    .baseUrl(CATBAG_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private RoutesGenerator() {
    }

    public static <S> S createRoutes(Class<S> serviceClass) {
        Retrofit retrofit = sBuilder.client(sHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
