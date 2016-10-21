package br.com.catbag.gifreduxsample.ui.giflist;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.customs.WakeActivityTestRule;
import br.com.catbag.gifreduxsample.ui.GifListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withBGColor;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withToast;
import static br.com.catbag.gifreduxsample.ui.giflist.mocks.FileDownloaderMocks.mockFileDownloaderToAlwaysFail;
import static br.com.catbag.gifreduxsample.ui.giflist.mocks.FileDownloaderMocks.mockFileDownloaderToDownloadInfinite;
import static br.com.catbag.gifreduxsample.ui.giflist.mocks.FileDownloaderMocks.removeMockInFileDownloader;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest {

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule = new WakeActivityTestRule<>(GifListActivity.class, false, false);


    @Test
    public void loadingDuringGifLoadingTest() {
        mockFileDownloaderToDownloadInfinite();

        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.loading))
                .check(matches(isDisplayed()));

        removeMockInFileDownloader();
    }

    @Test
    public void loadingAfterGifLoadingTest() {
        mActivityTestRule.launchActivity(new Intent());
        waitLoadGif();

        onView(withId(R.id.loading))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void notWatchedTest() {
        mActivityTestRule.launchActivity(new Intent());
        int expectedColor = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.notWatched);
        onView(withId(R.id.activity_gif_list))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void watchedGifTest() {
        mActivityTestRule.launchActivity(new Intent());
        waitLoadGif();

        int expectedColor = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.watched);
        onView(withId(R.id.gif_image))
               .perform(click());
        onView(withId(R.id.activity_gif_list))
                .check(matches(withBGColor(expectedColor)));
    }


    @Test
    public void playGifTest() {
        mActivityTestRule.launchActivity(new Intent());
        waitLoadGif();

        onView(withId(R.id.gif_image))
                .perform(click());
        assert(mActivityTestRule.getActivity().getGlideWrapper().getResource().isRunning());
    }

    @Test
    public void pauseGifTest() {
        mActivityTestRule.launchActivity(new Intent());
        waitLoadGif();

        onView(withId(R.id.gif_image))
                .perform(click());

        View image = mActivityTestRule.getActivity().findViewById(R.id.gif_image);
        Log.i("pauseGifTest", "before "+image.getWidth()+" W"+ image.getHeight()+ " H");
        sleep(2000);
        Log.i("pauseGifTest", "after "+image.getWidth()+" W"+ image.getHeight()+ " H");

        onView(withId(R.id.gif_image))
                .perform(click());
        assert(!mActivityTestRule.getActivity().getGlideWrapper().getResource().isRunning());
    }

    @Test
    public void gifDownloadFailedTest() {
        mockFileDownloaderToAlwaysFail();

        mActivityTestRule.launchActivity(new Intent());

        onView(withText("Download error")).inRoot(withToast())
                .check(matches(isDisplayed()));

        removeMockInFileDownloader();
    }

    private void waitSpinnerGone() {
        try {
            GifListActivity activity = mActivityTestRule.getActivity();
            boolean spinner = activity.findViewById(R.id.loading).getVisibility() == View.VISIBLE;
            while(spinner){
                spinner = activity.findViewById(R.id.loading).getVisibility() == View.VISIBLE;
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Log.e("GifListActivityTest", "", e);
        }
    }
    private void waitLoadGif() {
        try {
            GifListActivity activity = mActivityTestRule.getActivity();
            int width = 0;
            boolean spinner = activity.findViewById(R.id.loading).getVisibility() == View.VISIBLE;
            while(spinner || width == 0){
                width = activity.findViewById(R.id.gif_image).getWidth();
                spinner = activity.findViewById(R.id.loading).getVisibility() == View.VISIBLE;
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Log.e("GifListActivityTest", "", e);
        }
    }

    private void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(TAG, "", e);
        }
    }

}
