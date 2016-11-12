package br.com.catbag.gifreduxsample.matchers;

import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by niltonvasques on 10/14/16.
 */

public class Matchers {
    public static Matcher<View> withBGColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                int currentColor = ((ColorDrawable)view.getBackground()).getColor();
                return color == currentColor;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: " + color);
            }
        };
    }

    public static Matcher<View> withPlayingGifDrawable() {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                GifImageView gifImageView = (GifImageView) view.findViewById(R.id.gif_image);
                return ((GifDrawable)gifImageView.getDrawable()).isPlaying();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with gif image view playing");
            }
        };
    }

    public static Matcher<View> withEqualsDrawable(final Drawable drawable) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ImageView)) return false;
                return ((ImageView) view).getDrawable().equals(drawable);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with equals drawable");
            }
        };
    }

    public static Matcher<View> withDrawable() {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ImageView)) return false;
                return ((ImageView) view).getDrawable() != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with a not null drawable");
            }
        };
    }
}
