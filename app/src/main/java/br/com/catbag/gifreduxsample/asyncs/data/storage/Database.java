package br.com.catbag.gifreduxsample.asyncs.data.storage;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.IOException;

import br.com.catbag.gifreduxsample.models.AppState;

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

public class Database {

    private static final String TAG_APP_STATE = "TAG_APP_STATE";
    private Context mContext;

    public Database(final Context context) {
        mContext = context;
    }

    public AppState getAppState() throws SnappydbException, IOException {
        DB db = DBFactory.open(mContext);
        AppState appState = AppState.fromJson(db.get(TAG_APP_STATE));
        db.close();
        return appState;
    }

    public void saveAppState(AppState appState) throws SnappydbException, JsonProcessingException {
        DB db = DBFactory.open(mContext);
        db.put(TAG_APP_STATE, appState.toJson());
        db.close();
    }

}