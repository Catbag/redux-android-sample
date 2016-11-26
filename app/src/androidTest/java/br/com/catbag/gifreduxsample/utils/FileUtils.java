package br.com.catbag.gifreduxsample.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

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
