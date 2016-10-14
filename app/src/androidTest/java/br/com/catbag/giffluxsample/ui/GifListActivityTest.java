package br.com.catbag.giffluxsample.ui;


import android.app.Instrumentation;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.catbag.giffluxsample.R;

import static android.R.attr.animation;
import static android.R.attr.visibility;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest {

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule = new ActivityTestRule<>(GifListActivity.class);

    @Test
    public void loadingDuringGifLoadingTest() {
        onView(withId(R.id.loading))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loadingAfterGifLoadingTest() {
        waitLoadGif();

        onView(withId(R.id.loading))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void notWatchedTest() {
        int expectedColor = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.notWatched);
        onView(withId(R.id.activity_gif_list))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void watchedGifTest() {
        waitLoadGif();

        int expectedColor = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.watched);
        onView(withId(R.id.gif_image))
               .perform(click());
        onView(withId(R.id.activity_gif_list))
                .check(matches(withBGColor(expectedColor)));
    }


    @Test
    public void playGifTest() {
        waitLoadGif();

        onView(withId(R.id.gif_image))
                .perform(click());
        assert(mActivityTestRule.getActivity().getGlideWrapper().getResource().isRunning());
    }

    @Test
    public void pauseGifTest() {
        waitLoadGif();

        onView(withId(R.id.gif_image))
                .perform(click());

        onView(withId(R.id.gif_image))
                .perform(click());
        assert(!mActivityTestRule.getActivity().getGlideWrapper().getResource().isRunning());
    }

    private void waitLoadGif() {
        try {
            while(mActivityTestRule.getActivity().findViewById(R.id.loading).getVisibility() != View.GONE);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public static Matcher<View> withBGColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View warning) {
                int currentColor = ((ColorDrawable)warning.getBackground()).getColor();
                System.out.println("Current color: "+currentColor);
                return color == currentColor;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: "+color);
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
}
