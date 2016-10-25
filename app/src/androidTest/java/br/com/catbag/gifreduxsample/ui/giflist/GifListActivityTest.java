package br.com.catbag.gifreduxsample.ui.giflist;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;

import com.umaplay.fluxxan.Fluxxan;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.catbag.gifreduxsample.App;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.idlings.GlideLoadIdlingResource;
import br.com.catbag.gifreduxsample.idlings.GlidePlayIdlingResource;
import br.com.catbag.gifreduxsample.idlings.StartedActionIdlingResource;
import br.com.catbag.gifreduxsample.ui.GifListActivity;
import br.com.catbag.gifreduxsample.ui.wrappers.GifWrapper;

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
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest {

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule = new ActivityTestRule<>(GifListActivity.class, false, false);


    @Test
    public void loadingDuringGifLoadingTest() {
        mockFileDownloaderToDownloadInfinite();

        mActivityTestRule.launchActivity(new Intent());
        //waiting until dispatcher delivery the downloading action
        StartedActionIdlingResource startedResource = new StartedActionIdlingResource(getFluxxan());
        startedResource.registerIdlingResource();

        onView(withId(R.id.loading))
                .check(matches(isDisplayed()));

        startedResource.unregisterIdlingResource();
        removeMockInFileDownloader();
    }

    @Test
    public void loadingAfterGifLoadingTest() {
        mActivityTestRule.launchActivity(new Intent());

        GlideLoadIdlingResource loadResource = new GlideLoadIdlingResource(getWrapper());
        loadResource.registerIdlingResource();

        onView(withId(R.id.loading))
                .check(matches(not(isDisplayed())));

        loadResource.unregisterIdlingResource();
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

        GlideLoadIdlingResource loadResource = new GlideLoadIdlingResource(getWrapper());
        loadResource.registerIdlingResource();

        int expectedColor = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.watched);
        onView(withId(R.id.gif_image))
               .perform(click());
        onView(withId(R.id.activity_gif_list))
                .check(matches(withBGColor(expectedColor)));

        loadResource.unregisterIdlingResource();
    }


    @Test
    public void playGifTest() {
        mActivityTestRule.launchActivity(new Intent());
        GlideLoadIdlingResource loadResource = new GlideLoadIdlingResource(getWrapper());
        loadResource.registerIdlingResource();

        onView(withId(R.id.gif_image))
                .perform(click());
        assert(mActivityTestRule.getActivity().getGifWrapper().getDrawable().isRunning());

        loadResource.unregisterIdlingResource();
    }

    @Test
    public void pauseGifTest() {
        mActivityTestRule.launchActivity(new Intent());

        // Now we wait
        GlideLoadIdlingResource loadResource = new GlideLoadIdlingResource(getWrapper());
        loadResource.registerIdlingResource();

        onView(withId(R.id.gif_image))
                .perform(click());

        GlidePlayIdlingResource playResource = new GlidePlayIdlingResource(getWrapper());
        playResource.registerIdlingResource();

        onView(withId(R.id.gif_image))
                .perform(click());

        loadResource.unregisterIdlingResource();
        playResource.registerIdlingResource();

        assert(!mActivityTestRule.getActivity().getGifWrapper().getDrawable().isRunning());

    }

    @Test
    public void gifDownloadFailedTest() {
        mockFileDownloaderToAlwaysFail();

        mActivityTestRule.launchActivity(new Intent());

        onView(withText("Download error")).inRoot(withToast())
                .check(matches(isDisplayed()));

        removeMockInFileDownloader();
    }

    public GifWrapper getWrapper(){
        return mActivityTestRule.getActivity().getGifWrapper();
    }

    private Fluxxan getFluxxan(){
        return ((App) mActivityTestRule.getActivity().getApplication()).getFluxxan();
    }
}
