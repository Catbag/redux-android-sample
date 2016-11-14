package br.com.catbag.gifreduxsample.ui.giflist;


import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.idlings.AnvilTestLocker;
import br.com.catbag.gifreduxsample.idlings.FeedTestLocker;
import br.com.catbag.gifreduxsample.matchers.RecyclerViewMatcher;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.ui.GifListActivity;
import br.com.catbag.gifreduxsample.ui.components.FeedComponent;
import br.com.catbag.gifreduxsample.ui.components.GifComponent;
import shared.ReduxBaseTest;
import shared.TestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.view.View.generateViewId;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withBGColor;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withGifDrawable;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withPlayingGifDrawable;
import static br.com.catbag.gifreduxsample.utils.FileUtils.createFakeGifFile;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static shared.TestHelper.buildAppState;
import static shared.TestHelper.gifBuilderWithEmpty;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest extends ReduxBaseTest {

    public GifListActivityTest() {
        mHelper = new TestHelper(MyApp.getFluxxan());
    }

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule
            = new ActivityTestRule<>(GifListActivity.class, false, false);

    @Override
    public void setup() {
        super.setup();
        GifListActionCreator.getInstance().setDataManager(mock(DataManager.class));
    }

    @Override
    public void cleanup() {
        GifListActionCreator.getInstance().setDataManager(new DataManager());
        super.cleanup();
    }

    @Test
    public void whenGifIsDownloadingTest() {
        Gif gif = gifBuilderWithEmpty()
                .status(Gif.Status.DOWNLOADING)
                .build();

        mHelper.dispatchFakeAppState(buildAppState(gif));

        //Espresso waits for activity completely launch
        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.gif_loading)).check(matches(isDisplayed()));
    }

    @Test
    public void whenGifIsDownloadedTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = gifBuilderWithEmpty()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.DOWNLOADED)
                .build();

        mHelper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.gif_loading)).check(matches(not(isDisplayed())));

        onView(withId(R.id.gif_image)).check(matches(isDisplayed()));

        onView(withId(R.id.gif_image)).check(matches(withGifDrawable()));

        onView(withId(R.id.gif_image)).check(matches(not(withPlayingGifDrawable())));
    }

    @Test
    public void whenGifIsNotWatchedTest() {
        Gif gif = gifBuilderWithEmpty()
                .status(Gif.Status.DOWNLOADED)
                .watched(false)
                .build();

        mHelper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.notWatched);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenGifIsWatchedTest() {
        Gif gif = gifBuilderWithEmpty()
                .status(Gif.Status.DOWNLOADED)
                .watched(true)
                .build();

        mHelper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.watched);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenPlayGifTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = gifBuilderWithEmpty()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.DOWNLOADED)
                .build();

        mHelper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        AnvilTestLocker locker = new AnvilTestLocker(getGifComponent(0));

        onView(withId(R.id.gif_image)).perform(click());

        locker.registerIdlingResource();

        onView(withId(R.id.gif_image))
                .check(matches(withPlayingGifDrawable()));

        locker.unregisterIdlingResource();
    }

    @Test
    public void whenPauseGifTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = gifBuilderWithEmpty()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.LOOPING)
                .build();

        mHelper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        AnvilTestLocker locker = new AnvilTestLocker(getGifComponent(0));

        onView(withId(R.id.gif_image)).perform(click());

        locker.registerIdlingResource();

        onView(withId(R.id.gif_image))
                .check(matches(not(withPlayingGifDrawable())));

        locker.unregisterIdlingResource();
    }

    @Test
    public void whenGifDownloadFailTest() {
        Gif gif = gifBuilderWithEmpty()
                .status(Gif.Status.DOWNLOAD_FAILED)
                .build();

        mHelper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.error);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenLoadMultipleGifsOnAppStateTest() {
        int gifListSize = 5;
        mHelper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        for (int i = 0; i < gifListSize; i++) {
            onView(withId(getRecyclerView().getId())).perform(scrollToPosition(i));
        }

        assertEquals(gifListSize, getRecyclerView().getAdapter().getItemCount());
    }

    @Test
    public void whenScrollDownAndPlayLastItemTest() {
        int gifListSize = 5;
        mHelper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        //Create a locker that waits X times feed rendering (x = count of gifs on screen)
        FeedTestLocker scrollLocker = new FeedTestLocker(getFeedComponent(),
                getRecyclerView().getChildCount());

        int lastAdapterPos = gifListSize-1;
        //Scroll to last adapter item
        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(lastAdapterPos));

        //Wait for scroll consequent rendering to finish
        scrollLocker.registerIdlingResource();
        espressoWaiter();
        scrollLocker.unregisterIdlingResource();

        int lastScreenPos = getRecyclerView().getChildCount()-1;
        //Initiate lock on bottom GifComponent
        AnvilTestLocker playLocker
                = new AnvilTestLocker(getGifComponent(lastScreenPos));

        //Click on GifComponent that represents the last adapter item.
        onView(withId(getRecyclerView().getId()))
                .perform(actionOnItemAtPosition(lastAdapterPos, click()));

        playLocker.registerIdlingResource();

        //Checks if it has a playing status
        onView(withRecyclerView(getRecyclerView().getId())
                .atPosition(lastScreenPos))
                .check(matches(withPlayingGifDrawable()));

        playLocker.unregisterIdlingResource();
    }

    @Test
    public void whenScrollUpAndPlayFirstItemTest() {
        int gifListSize = 5;
        mHelper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        //Create a locker that waits X times feed rendering (x = count of gifs on screen)
        FeedTestLocker scrollLocker = new FeedTestLocker(getFeedComponent(),
                getRecyclerView().getChildCount());

        //Scroll to last adapter item
        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(gifListSize-1));

        //Wait for scroll consequent rendering to finish
        scrollLocker.registerIdlingResource();
        espressoWaiter();
        scrollLocker.unregisterIdlingResource();

        //Scroll to first adapter item (no need to wait: top items already rendered)
        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(0));

        //Initiate lock on top GifComponent
        AnvilTestLocker playLocker
                = new AnvilTestLocker(getGifComponent(0));

        //Click on GifComponent that represents the first adapter item.
        onView(withId(getRecyclerView().getId()))
                .perform(actionOnItemAtPosition(0, click()));

        playLocker.registerIdlingResource();

        //Checks if it has a playing status
        onView(withRecyclerView(getRecyclerView().getId())
                .atPosition(0))
                .check(matches(withPlayingGifDrawable()));

        playLocker.unregisterIdlingResource();
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private FeedComponent getFeedComponent() {
        return (FeedComponent) getActivity().findViewById(R.id.feed);
    }

    private RecyclerView getRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) getFeedComponent().getChildAt(0);
        recyclerView.setId(generateViewId());
        return recyclerView;
    }

    private GifComponent getGifComponent(int screenPos) {
        FrameLayout frameLayout = (FrameLayout) getRecyclerView().getChildAt(screenPos);
        return (GifComponent) frameLayout.getChildAt(0);
    }

    private GifListActivity getActivity() {
        return mActivityTestRule.getActivity();
    }

    private List<Gif> createFakeGifList(int size) {
        List<Gif> gifs = new ArrayList<>();
        File fakeGifFile = createFakeGifFile();
        for (int i = 0; i < size; i++) {
            Gif gif = gifBuilderWithEmpty()
                    .title("gif"+i)
                    .status(Gif.Status.DOWNLOADED)
                    .path(fakeGifFile.getAbsolutePath())
                    .build();
            gifs.add(gif);
        }

        return gifs;
    }

    private void espressoWaiter() {
        onView(withText("espressoWaiter")).check(doesNotExist());
    }
}
