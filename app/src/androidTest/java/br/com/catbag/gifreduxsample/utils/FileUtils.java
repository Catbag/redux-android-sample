package br.com.catbag.gifreduxsample.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by niltonvasques on 10/27/16.
 */

public final class FileUtils {

    private FileUtils() { }

    public static File createFakeGifFile() {
        File dst = new File(getInstrumentation().getTargetContext()
                .getExternalCacheDir() + "/test.gif");
        try {
            InputStream iStream = getInstrumentation().getContext()
                    .getResources().getAssets().open("test.gif");
            OutputStream out = new FileOutputStream(dst);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = iStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            iStream.close();
            out.close();
        } catch (IOException e) {
            Log.e(FileUtils.class.getSimpleName(), "", e);
        }
        return dst;
    }

}
