package br.com.catbag.gifreduxsample.asyncs.data.storage;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.IOException;

import br.com.catbag.gifreduxsample.models.AppState;

/**
 * Created by felipe on 26/10/16.
 */

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