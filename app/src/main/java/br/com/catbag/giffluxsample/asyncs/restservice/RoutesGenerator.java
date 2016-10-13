package br.com.catbag.giffluxsample.asyncs.restservice;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by felipe on 13/10/16.
 */

public class RoutesGenerator {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final String CATBAG_URL = "http://www.catbag.com.br";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(CATBAG_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createRoutes(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
