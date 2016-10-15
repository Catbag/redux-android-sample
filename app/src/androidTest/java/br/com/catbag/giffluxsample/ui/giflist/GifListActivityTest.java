package br.com.catbag.giffluxsample.ui.giflist;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.catbag.giffluxsample.R;
import br.com.catbag.giffluxsample.actions.GifActionCreator;
import br.com.catbag.giffluxsample.asyncs.restservice.FileDownloader;
import br.com.catbag.giffluxsample.ui.GifListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.catbag.giffluxsample.matchers.Matchers.withBGColor;
import static br.com.catbag.giffluxsample.matchers.Matchers.withToast;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest {

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule = new ActivityTestRule<>(GifListActivity.class, false, false);

    @Test
    public void loadingDuringGifLoadingTest() {
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.loading))
                .check(matches(isDisplayed()));
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

        onView(withId(R.id.gif_image))
                .perform(click());
        assert(!mActivityTestRule.getActivity().getGlideWrapper().getResource().isRunning());
    }

    private FileDownloader.FailureDownloadListener listener;
    private FileDownloader downloader;
    public void mockFileDownloaderToAlwaysFail(){
        downloader = mock(FileDownloader.class);
        doAnswer( (invocation) -> {
            System.out.println("MOCKED METHOD");
            listener.onFailure(new Exception("Download error"));
            return null;
        }).when(downloader).download(anyString(), anyString());
        doAnswer( (invocation) -> {
            Object[] args = invocation.getArguments();
            Object mock = invocation.getMock();
            System.out.println("MOCKED FAILURE");
            listener = (FileDownloader.FailureDownloadListener)args[0];
            return mock;
        }).when(downloader).onFailure(any(FileDownloader.FailureDownloadListener.class));
        doReturn(downloader).when(downloader).onSuccess(any(FileDownloader.SuccessDownloadListener.class));
        doReturn(downloader).when(downloader).onStarted(any(FileDownloader.StartDownloadListener.class));

        GifActionCreator.getInstance().setFileDownloader(downloader);
    }

    public void removeMockInFileDownloader(){
        GifActionCreator.getInstance().setFileDownloader(new FileDownloader());
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
