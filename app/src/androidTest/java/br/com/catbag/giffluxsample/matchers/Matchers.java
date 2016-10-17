package br.com.catbag.giffluxsample.matchers;

import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.view.View;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by niltonvasques on 10/14/16.
 */

public class Matchers {
    public static Matcher<View> withBGColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View warning) {
                int currentColor = ((ColorDrawable)warning.getBackground()).getColor();
                return color == currentColor;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: " + color);
            }
        };
    }

    public static Matcher<View> animationRunning() {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View warning) {
                if(warning.getAnimation() == null) return false;
                return warning.getAnimation().isInitialized();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("animation running");
            }
        };
    }

    public static TypeSafeMatcher<Root> withToast(){
        return new ToastMatcher();

    }

    public static class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    //means this window isn't contained by any other windows.
                    return true;
                }
            }
            return false;
        }
    }
}
