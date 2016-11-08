package br.com.catbag.gifreduxsample.ui.giflist;


import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.idlings.UiTestLocker;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.ui.GifListActivity;
import br.com.catbag.gifreduxsample.ui.components.FeedComponent;
import br.com.catbag.gifreduxsample.ui.components.GifComponent;
import pl.droidsonroids.gif.GifImageView;
import shared.FakeReducer;
import shared.TestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.view.View.generateViewId;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withBGColor;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withGifDrawable;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withPlayingGifDrawable;
import static br.com.catbag.gifreduxsample.utils.FileUtils.createFakeGifFile;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static shared.TestHelper.buildAppState;
import static shared.TestHelper.builderWithEmpty;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest {

    private TestHelper helper = new TestHelper(MyApp.getFluxxan());

    @Rule
    public Timeout mGlobalTimeout = new Timeout(20, TimeUnit.SECONDS);

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule
            = new ActivityTestRule<>(GifListActivity.class, false, false);

    @Before
    public void setUp() {
        GifListActionCreator.getInstance().setDataManager(mock(DataManager.class));
        helper.getFluxxan().registerReducer(new FakeReducer());
        helper.activateStateListener();
    }

    @After
    public void cleanUp() {
        helper.deactivateStateListener();
        helper.getFluxxan().unregisterReducer(FakeReducer.class);
        GifListActionCreator.getInstance().setDataManager(new DataManager());
    }

    @Test
    public void whenGifIsDownloadingTest() {
        Gif gif = builderWithEmpty()
                .status(Gif.Status.DOWNLOADING)
                .build();

        helper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.gif_loading)).check(matches(isDisplayed()));
    }

    @Test
    public void whenGifIsDownloadedTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = builderWithEmpty()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.DOWNLOADED)
                .build();

        helper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.gif_loading)).check(matches(not(isDisplayed())));

        onView(withId(R.id.gif_image)).check(matches(isDisplayed()));

        onView(withId(R.id.gif_image)).check(matches(withGifDrawable()));

        onView(withId(R.id.gif_image)).check(matches(not(withPlayingGifDrawable())));
    }

    @Test
    public void whenGifIsNotWatchedTest() {
        Gif gif = builderWithEmpty()
                .status(Gif.Status.DOWNLOADED)
                .watched(false)
                .build();

        helper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.notWatched);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenGifIsWatchedTest() {
        Gif gif = builderWithEmpty()
                .status(Gif.Status.DOWNLOADED)
                .watched(true)
                .build();

        helper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.watched);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenPlayGifTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = builderWithEmpty()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.DOWNLOADED)
                .build();

        helper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        UiTestLocker locker = new UiTestLocker(getGifComponent(0));

        onView(withId(R.id.gif_image)).perform(click());

        locker.registerIdlingResource();

        onView(withId(R.id.gif_image))
                .check(matches(withPlayingGifDrawable()));

        locker.unregisterIdlingResource();
    }

    @Test
    public void whenPauseGifTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = builderWithEmpty()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.LOOPING)
                .build();

        helper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        UiTestLocker locker = new UiTestLocker(getGifComponent(0));

        onView(withId(R.id.gif_image)).perform(click());

        locker.registerIdlingResource();

        onView(withId(R.id.gif_image))
                .check(matches(not(withPlayingGifDrawable())));

        locker.unregisterIdlingResource();
    }

    @Test
    public void whenGifDownloadFailTest() {
        Gif gif = builderWithEmpty()
                .status(Gif.Status.DOWNLOAD_FAILED)
                .build();

        helper.dispatchFakeAppState(buildAppState(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.error);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenLoadMultipleGifsOnAppStateTest() {
        int gifListSize = 5;
        helper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        for (int i = 0; i < gifListSize; i++) {
            onView(withId(getRecyclerView().getId())).perform(scrollToPosition(i));
        }

        assertEquals(gifListSize, getRecyclerView().getAdapter().getItemCount());
    }

    @Test
    public void whenScrollDownAndPlayLastItemTest() {
        int gifListSize = 5;
        helper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        int lastItemPos = getRecyclerView().getChildCount()-1;
        //Initiate lock on first adapter GifComponent
        UiTestLocker locker = new UiTestLocker(getGifComponent(lastItemPos));

        //Scroll to bottom and click on last adapter GifComponent
        onView(withId(getRecyclerView().getId()))
                .perform(actionOnItemAtPosition(gifListSize-1, click()));

        locker.registerIdlingResource();

        //Get the first GifImageView and set a unique id
        int tempId = generateViewId();
        getGifImgViewAt(R.id.gif_image, lastItemPos).setId(tempId);

        //Checks if it has a playing status
        onView(withId(tempId))
                .check(matches(withPlayingGifDrawable()));

        //clean test modifications
        getGifImgViewAt(tempId, lastItemPos).setId(R.id.gif_image);
        locker.unregisterIdlingResource();
    }

    @Test
    public void whenScrollUpAndPlayFirstItemTest() {
        int gifListSize = 5;
        helper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        //Initiate lock on first adapter GifComponent
        UiTestLocker locker = new UiTestLocker(getGifComponent(0));

        //Scroll to bottom
        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(gifListSize-1));

        //Scroll to top and click on first adapter GifComponent
        onView(withId(getRecyclerView().getId()))
                .perform(actionOnItemAtPosition(0, click()));

        locker.registerIdlingResource();

        //Get the first GifImageView and set a unique id
        int tempId = generateViewId();
        getGifImgViewAt(R.id.gif_image, 0).setId(tempId);

        //Checks if it has a playing status
        onView(withId(tempId)).check(matches(withPlayingGifDrawable()));

        //clean test modifications
        getGifImgViewAt(tempId, 0).setId(R.id.gif_image);
        locker.unregisterIdlingResource();
    }

    private RecyclerView getRecyclerView() {
        FeedComponent feed = (FeedComponent) getActivity().findViewById(R.id.feed);
        RecyclerView recyclerView = (RecyclerView) feed.getChildAt(0);
        recyclerView.setId(generateViewId());
        return recyclerView;
    }

    private GifComponent getGifComponent(int screenPos) {
        FrameLayout frameLayout = (FrameLayout) getRecyclerView().getChildAt(screenPos);
        return (GifComponent) frameLayout.getChildAt(0);
    }

    private GifImageView getGifImgViewAt(int viewId, int screenPos) {
        FrameLayout frameLayout = (FrameLayout) getRecyclerView().getChildAt(screenPos);
        return (GifImageView) frameLayout.findViewById(viewId);
    }

    private GifListActivity getActivity() {
        return mActivityTestRule.getActivity();
    }

    private List<Gif> createFakeGifList(int size) {
        List<Gif> gifs = new ArrayList<>();
        File fakeGifFile = createFakeGifFile();
        for (int i = 0; i < size; i++) {
            Gif gif = builderWithEmpty()
                    .title("gif"+i)
                    .status(Gif.Status.DOWNLOADED)
                    .path(fakeGifFile.getAbsolutePath())
                    .build();
            gifs.add(gif);
        }

        return gifs;
    }
}
