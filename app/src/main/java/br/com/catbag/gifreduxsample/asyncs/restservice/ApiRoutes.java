package br.com.catbag.gifreduxsample.asyncs.restservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by felipe on 13/10/16.
 */

public interface ApiRoutes {
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
