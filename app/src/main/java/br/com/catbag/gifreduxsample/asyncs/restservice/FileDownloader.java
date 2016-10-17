package br.com.catbag.gifreduxsample.asyncs.restservice;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

import br.com.catbag.gifreduxsample.asyncs.files.FileWriter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by felipe on 13/10/16.
 */

public class FileDownloader {
    private static final String TAG = "FileDownloader";
    private final ApiRoutes mRoutes;
    private SuccessDownloadListener mSuccessListener;
    private FailureDownloadListener mFailureListener;
    private StartDownloadListener mStartListener;

    public FileDownloader() {
        mRoutes = RoutesGenerator.createRoutes(ApiRoutes.class);
    }

    public void download(String fileUrl, String pathToSave) {
        Call<ResponseBody> call = mRoutes.downloadFileWithDynamicUrlSync(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    try {
                        FileWriter.writeToDisk(response.body().byteStream(),
                                response.body().contentLength(), pathToSave);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mFailureListener != null) {
                            mFailureListener.onFailure(new FileNotFoundException());
                        }
                        Log.d(TAG, "file download failed");
                    }

                    Log.d(TAG, "file download was a success");

                    if (mSuccessListener != null) mSuccessListener.onSuccess();

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
                mFailureListener.onFailure(new Exception(t));
            }
        });

        if (mStartListener != null) mStartListener.onStarted();
    }

    public FileDownloader onSuccess(SuccessDownloadListener listener) {
        mSuccessListener = listener;
        return this;
    }

    public FileDownloader onFailure(FailureDownloadListener listener) {
        mFailureListener = listener;
        return this;
    }

    public FileDownloader onStarted(StartDownloadListener listener) {
        mStartListener = listener;
        return this;
    }

    public interface SuccessDownloadListener {
        void onSuccess();
    }

    public interface FailureDownloadListener {
        void onFailure(Exception e);
    }

    public interface StartDownloadListener {
        void onStarted();
    }
}
