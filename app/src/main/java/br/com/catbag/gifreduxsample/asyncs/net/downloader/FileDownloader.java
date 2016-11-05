package br.com.catbag.gifreduxsample.asyncs.net.downloader;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.catbag.gifreduxsample.asyncs.net.rest.retrofit.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by felipe on 13/10/16.
 */

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
                            failureDownloadListener.onFailure(new FileNotFoundException("Not found"));
                        }
                    }

                    if (successDownloadListener != null) successDownloadListener.onSuccess();
                }
                else if (failureDownloadListener != null) {
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

    public void readFromNetAndWriteToDisk(InputStream inputStream, String pathToSave)
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
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    public interface SuccessDownloadListener {
        void onSuccess();
    }

    public interface FailureDownloadListener {
        void onFailure(Exception e);
    }

}
