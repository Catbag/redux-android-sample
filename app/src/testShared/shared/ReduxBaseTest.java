package shared;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

/**
 * Created by raulcca on 11/10/16.
 */

public class ReduxBaseTest {
    @Rule
    public Timeout mGlobalTimeout = new Timeout(1, TimeUnit.MINUTES);

    protected TestHelper mHelper = null;

    @Before
    public void setup() {
        if (mHelper != null) {
            mHelper.getFluxxan().registerReducer(new FakeReducer());
            mHelper.activateStateListener();
        } else {
            informTestHelperNotInitialized();
        }
    }

    @After
    public void cleanup() {
        if (mHelper != null) {
            mHelper.deactivateStateListener();
            mHelper.getFluxxan().unregisterReducer(FakeReducer.class);
        } else {
            informTestHelperNotInitialized();
        }
    }

    private void informTestHelperNotInitialized() {
        Log.e(getClass().getSimpleName(),
                "TestHelper not initialized: set super.mHelper with properly Fluxxan instance");
    }
}
