package br.com.catbag.gifreduxsample;

import android.app.Application;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.Middleware;

import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.asyncs.data.net.downloader.FileDownloader;
import br.com.catbag.gifreduxsample.middlewares.PersistenceMiddleware;
import br.com.catbag.gifreduxsample.middlewares.RestMiddleware;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.reducers.AppStateReducer;

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

public class MyApp extends Application {

    private static Fluxxan<AppState> sFluxxan = null;
    private PersistenceMiddleware mPersistenceMiddleware = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFluxxan();
    }

    private void initializeFluxxan() {
        sFluxxan = new MyFluxxan(ImmutableAppState.builder().build());
        sFluxxan.registerReducer(new AppStateReducer());

        DataManager dm = new DataManager(getBaseContext());

        Middleware restMiddleware = new RestMiddleware(getBaseContext(), dm, new FileDownloader());
        sFluxxan.getDispatcher().registerMiddleware(restMiddleware);

        mPersistenceMiddleware = new PersistenceMiddleware(dm);
        sFluxxan.getDispatcher().registerMiddleware(mPersistenceMiddleware);
        sFluxxan.addListener(mPersistenceMiddleware);
        sFluxxan.start();
    }

    public void onTerminate() {
        sFluxxan.removeListener(mPersistenceMiddleware);
        sFluxxan.stop();
        super.onTerminate();
    }

    public static Fluxxan<AppState> getFluxxan() {
        return sFluxxan;
    }
}
