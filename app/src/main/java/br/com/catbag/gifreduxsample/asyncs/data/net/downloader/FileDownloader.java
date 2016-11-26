package br.com.catbag.gifreduxsample.asyncs.data.net.downloader;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.retrofit.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

public class FileDownloader {

    private static RetrofitBuilder.DownloadRoute sRoutes;

    public FileDownloader() {
        sRoutes = RetrofitBuilder.getInstance().createDownloadEndpoint();
    }

    public void download(String fileUrl, String pathToSave,
                         SuccessDownloadListener successDownloadListener,
                         FailureDownloadListener failureDownloadListener) {
        Call<ResponseBody> call = sRoutes.downloadFileWithDynamicUrlSync(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        readFromNetAndWriteToDisk(response.body().byteStream(), pathToSave);
                    } catch (IOException e) {
                        if (failureDownloadListener != null) {
                            failureDownloadListener
                                    .onFailure(new FileNotFoundException("Not found"));
                        }
                    }

                    if (successDownloadListener != null) successDownloadListener.onSuccess();
                } else if (failureDownloadListener != null) {
                    failureDownloadListener.onFailure(new FileNotFoundException("Not found"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(FileDownloader.class.getSimpleName(), "error");
                failureDownloadListener.onFailure(new Exception(t));
            }
        });
    }

    private void readFromNetAndWriteToDisk(InputStream inputStream, String pathToSave)
            throws IOException {

        File futureStudioIconFile = new File(pathToSave);
        OutputStream outputStream = new FileOutputStream(futureStudioIconFile);
        byte[] fileReader = new byte[4096];

        while (true) {
            //Here the download is done...if some progress was made, here is the local
            int read = inputStream.read(fileReader);

            if (read == -1) {
                break;
            }
            outputStream.write(fileReader, 0, read);
        }

        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    public interface SuccessDownloadListener {
        void onSuccess();
    }

    public interface FailureDownloadListener {
        void onFailure(Exception e);
    }

}
