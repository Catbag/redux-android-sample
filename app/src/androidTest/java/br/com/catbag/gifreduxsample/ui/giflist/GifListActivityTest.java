package br.com.catbag.gifreduxsample.ui.giflist;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.idlings.AnvilTestLocker;
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
import static br.com.catbag.gifreduxsample.matchers.Matchers.withEqualsGifDrawable;
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
        MyApp.setDataManager(mock(DataManager.class));
    }

    @Override
    public void cleanup() {
        MyApp.setDataManager(new DataManager(InstrumentationRegistry.getTargetContext()));
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
        int gifListSize = 10;
        mHelper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        assertEquals(gifListSize, getRecyclerView().getAdapter().getItemCount());

        for (int i = 0; i < gifListSize; i++) {
            for (int j = 0; j < getRecyclerView().getChildCount(); j++) {
                onView(withRecyclerPos(j)).check(matches(withGifDrawable()));
            }

            int nextScrollingPos = i + getRecyclerView().getChildCount();
            if (nextScrollingPos > gifListSize - 1) {
                break;
            }

            AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedComponent());

            onView(withId(getRecyclerView().getId())).perform(scrollToPosition(nextScrollingPos));

            espressoWaiter(scrollLocker);
        }
    }

    @Test
    public void whenScrollDownAndPlayLastItemTest() {
        int gifListSize = 5;
        mHelper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedComponent());

        int lastAdapterPos = gifListSize-1;
        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(lastAdapterPos));

        espressoWaiter(scrollLocker);

        int lastScreenPos = getRecyclerView().getChildCount()-1;
        AnvilTestLocker playLocker = new AnvilTestLocker(getGifComponent(lastScreenPos));

        onView(withId(getRecyclerView().getId()))
                .perform(actionOnItemAtPosition(lastAdapterPos, click()));

        playLocker.registerIdlingResource();

        onView(withRecyclerPos(lastScreenPos)).check(matches(withPlayingGifDrawable()));

        playLocker.unregisterIdlingResource();
    }

    @Test
    public void whenScrollUpAndPlayFirstItemTest() {
        int gifListSize = 5;
        mHelper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedComponent());

        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(gifListSize-1));

        espressoWaiter(scrollLocker);

        scrollLocker = new AnvilTestLocker(getFeedComponent());

        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(0));

        espressoWaiter(scrollLocker);

        AnvilTestLocker playLocker = new AnvilTestLocker(getGifComponent(0));

        onView(withId(getRecyclerView().getId()))
                .perform(actionOnItemAtPosition(0, click()));

        playLocker.registerIdlingResource();

        onView(withRecyclerPos(0)).check(matches(withPlayingGifDrawable()));

        playLocker.unregisterIdlingResource();
    }

    @Test
    public void whenListKeepOrderedAfterPlayTest() {
        int gifListSize = 20;
        mHelper.dispatchFakeAppState(buildAppState(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        for (int i = 0; i < gifListSize-1; i++) {
            List<Drawable> drawablesBackup = getAllGifsDrawableOnScreen();

            AnvilTestLocker playLocker = new AnvilTestLocker(getGifComponent(0));

            onView(withId(getRecyclerView().getId()))
                    .perform(actionOnItemAtPosition(i, click()));

            playLocker.registerIdlingResource();
            for (int j = 0; j < getRecyclerView().getChildCount(); j++) {
                onView(withRecyclerPos(j)).check(matches(withEqualsGifDrawable(drawablesBackup.get(j))));
            }
            playLocker.unregisterIdlingResource();

            if (i + 1 < gifListSize-1) {
                AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedComponent());

                onView(withId(getRecyclerView().getId())).perform(scrollToPosition(i + 1));

                espressoWaiter(scrollLocker);
            }
        }
    }

    private List<Drawable> getAllGifsDrawableOnScreen(){
        List<Drawable> drawables = new ArrayList<>();
        for (int x = 0; x < getRecyclerView().getChildCount(); x++) {
            ImageView gifImage = (ImageView) getGifComponent(x).findViewById(R.id.gif_image);
            drawables.add(gifImage.getDrawable());
        }
        return drawables;
    }


    //Get item at screen position
    private Matcher<View> withRecyclerPos(int pos) {
        return new RecyclerViewMatcher(getRecyclerView().getId()).atPosition(pos);
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

    private void espressoWaiter(AnvilTestLocker locker) {
        locker.registerIdlingResource();
        onView(withText("espressoWaiter")).check(doesNotExist());
        locker.unregisterIdlingResource();
    }
}
