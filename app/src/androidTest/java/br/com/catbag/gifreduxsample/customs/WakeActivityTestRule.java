package br.com.catbag.gifreduxsample.customs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.view.WindowManager;

/**
 * Created by niltonvasques on 10/20/16.
 */

public class WakeActivityTestRule<T extends Activity> extends ActivityTestRule<T>{
    public WakeActivityTestRule(Class<T> activityClass) {
        super(activityClass);
    }

    public WakeActivityTestRule(Class<T> activityClass, boolean initialTouchMode) {
        super(activityClass, initialTouchMode);
    }

    public WakeActivityTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
        super(activityClass, initialTouchMode, launchActivity);
    }

    @Override
    public T launchActivity(@Nullable Intent startIntent) {
        T activity = super.launchActivity(startIntent);
        setUp(activity);
        return activity;
    }

    private void setUp(T activity) {
        //This code unlock the device if it was locked (for CI emulator tools)
        Runnable wakeUpDevice = () -> activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.runOnUiThread(wakeUpDevice);
    }
}
