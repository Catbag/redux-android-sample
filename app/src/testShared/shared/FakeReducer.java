package shared;

import com.umaplay.fluxxan.annotation.BindAction;
import com.umaplay.fluxxan.impl.BaseAnnotatedReducer;

import br.com.catbag.gifreduxsample.models.AppState;

/**
 * Created by niltonvasques on 10/27/16.
 */

public class FakeReducer extends BaseAnnotatedReducer<AppState> {


    public static final String FAKE_REDUCE_ACTION = "FAKE_REDUCE_ACTION";

    @BindAction(FAKE_REDUCE_ACTION)
    public AppState fakeReduce(AppState oldState, AppState newState) {
        return newState;
    }
}
