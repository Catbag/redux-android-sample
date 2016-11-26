package br.com.catbag.gifreduxsample.matchers;

import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.view.View;
import android.widget.FrameLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.ui.views.GifView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

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

public final class Matchers {
    private Matchers() {
    }

    public static Matcher<View> withBGColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                int currentColor = ((ColorDrawable) view.getBackground()).getColor();
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
                return ((GifDrawable) gifImageView.getDrawable()).isPlaying();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with gif image view playing");
            }
        };
    }

    public static Matcher<View> withEqualsGifUuid(final String uuid) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                GifView gifView = (GifView) ((FrameLayout) view).getChildAt(0);
                return gifView.getGif().getUuid().equals(uuid);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with equals Gif UUID");
            }
        };
    }

    public static Matcher<View> withGifDrawable() {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                GifImageView gifImageView = (GifImageView) view.findViewById(R.id.gif_image);
                return gifImageView.getDrawable() != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with a not null drawable");
            }
        };
    }
}
