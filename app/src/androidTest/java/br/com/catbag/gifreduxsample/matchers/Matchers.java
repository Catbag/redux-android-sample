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
                GifImageView gifImageView = (GifImageView) view;
                return ((GifDrawable)gifImageView.getDrawable()).isPlaying();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with gif image view playing");
            }
        };
    }

    public static Matcher<View> withGifDrawable() {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                GifImageView gifImageView = (GifImageView) view;
                return gifImageView.getDrawable() != null;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with gif image view not null");
            }
        };
    }
}
