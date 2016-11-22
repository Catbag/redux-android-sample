package br.com.catbag.gifreduxsample.middlewares;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.StateListener;
import com.umaplay.fluxxan.impl.BaseMiddleware;

import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.models.AppState;

import static br.com.catbag.gifreduxsample.actions.AppStateActionCreator.APP_STATE_LOAD;
import static br.com.catbag.gifreduxsample.actions.AppStateActionCreator.APP_STATE_LOADED;

/**
 * Created by felipe on 22/11/16.
 */

public class PersistenceMiddleware extends BaseMiddleware<AppState> implements
        StateListener<AppState> {

    private DataManager mDataManager;

    public PersistenceMiddleware(DataManager dataManager) {
        super();
        mDataManager = dataManager;
    }

    @Override
    public void intercept(AppState appState, Action action) throws Exception {
        switch(action.Type) {
            case APP_STATE_LOAD:
                loadAppState();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean hasStateChanged(AppState newState, AppState oldState) {
        return newState != oldState;
    }

    @Override
    public void onStateChanged(AppState appState) {
        mDataManager.saveAppState(appState);
    }

    private void loadAppState() {
        mDataManager.loadAppState(appState -> mDispatcher.dispatch(
                new Action(APP_STATE_LOADED, appState)));
    }

}
