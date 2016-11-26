package br.com.catbag.gifreduxsample.middlewares;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.StateListener;
import com.umaplay.fluxxan.impl.BaseMiddleware;

import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.models.AppState;

import static br.com.catbag.gifreduxsample.actions.AppStateActionCreator.APP_STATE_LOAD;
import static br.com.catbag.gifreduxsample.actions.AppStateActionCreator.APP_STATE_LOADED;

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
