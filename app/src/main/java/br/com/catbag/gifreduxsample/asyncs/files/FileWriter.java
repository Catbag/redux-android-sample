package br.com.catbag.gifreduxsample.asyncs.files;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by felipe on 13/10/16.
 */

public class FileWriter {
    private static final String TAG = "FileWriter";

    public static void writeToDisk(InputStream inputStream, long fileSize, String pathToSave)
            throws IOException {

        File futureStudioIconFile = new File(pathToSave);
        OutputStream outputStream = new FileOutputStream(futureStudioIconFile);
        byte[] fileReader = new byte[4096];
        long fileSizeDownloaded = 0;

        while (true) {
            int read = inputStream.read(fileReader);

            if (read == -1) {
                break;
            }
            outputStream.write(fileReader, 0, read);
            fileSizeDownloaded += read;
            Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
        }

        outputStream.flush();
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
