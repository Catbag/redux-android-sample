package br.com.catbag.gifreduxsample.ui.giflist;


import android.content.Intent;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.umaplay.fluxxan.impl.DispatcherImpl;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.lockers.AnvilTestLocker;
import br.com.catbag.gifreduxsample.lockers.RecyclerTestLocker;
import br.com.catbag.gifreduxsample.matchers.RecyclerViewMatcher;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.ui.GifListActivity;
import br.com.catbag.gifreduxsample.ui.views.FeedView;
import br.com.catbag.gifreduxsample.ui.views.GifView;
import br.com.catbag.gifreduxsample.ui.views.GifsAdapter;
import shared.ReduxBaseTest;
import shared.TestHelper;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.view.View.generateViewId;
import static br.com.catbag.gifreduxsample.MyApp.getFluxxan;
import static br.com.catbag.gifreduxsample.asyncs.data.DataManager.getAppStateDefault;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withBGColor;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withEqualsGifUuid;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withGifDrawable;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withPlayingGifDrawable;
import static br.com.catbag.gifreduxsample.utils.FileUtils.createFakeGifFile;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static shared.TestHelper.buildAppState;
import static shared.TestHelper.gifBuilderWithEmpty;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest extends ReduxBaseTest {

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule
            = new ActivityTestRule<>(GifListActivity.class, false, false);

    public GifListActivityTest() {
        mHelper = new TestHelper(getFluxxan());
    }

    @Override
    public void setup() {
        super.setup();
        mHelper.replaceMiddlewares(mock(DataManager.class), getTargetContext());
    }

    @Override
    public void cleanup() {
        mHelper.replaceMiddlewares(new DataManager(getTargetContext()), getTargetContext());
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

        onView(withId(R.id.gif_item)).check(matches(withBGColor(expectedColor)));
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

        onView(withId(R.id.gif_item)).check(matches(withBGColor(expectedColor)));
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

        AnvilTestLocker locker = new AnvilTestLocker(getGifView(0));

        onView(withId(R.id.gif_image)).perform(click());

        locker.registerIdlingResource();

        onView(withId(R.id.gif_image)).check(matches(withPlayingGifDrawable()));

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

        AnvilTestLocker locker = new AnvilTestLocker(getGifView(0));

        onView(withId(R.id.gif_image)).perform(click());

        locker.registerIdlingResource();

        onView(withId(R.id.gif_image)).check(matches(not(withPlayingGifDrawable())));

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

        onView(withId(R.id.gif_item)).check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenGifListIsEmpty() {
        mHelper.dispatchFakeAppState(ImmutableAppState.builder().build());

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.loading))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void whenGifListIsNotEmpty() {
        mHelper.dispatchFakeAppState(buildAppState(createFakeDownloadedGifList(5)));

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.loading))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void whenLoadMultipleGifsOnAppStateTest() {
        int gifListSize = 10;
        mHelper.dispatchFakeAppState(buildAppState(createFakeDownloadedGifList(gifListSize)));

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

            AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedView());

            onView(withId(getRecyclerView().getId())).perform(scrollToPosition(nextScrollingPos));

            scrollLocker.waitForEspresso();
        }
    }

    @Test
    public void whenScrollDownAndPlayLastItemTest() {
        int gifListSize = 5;
        mHelper.dispatchFakeAppState(buildAppState(createFakeDownloadedGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedView());

        int lastAdapterPos = gifListSize - 1;
        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(lastAdapterPos));

        scrollLocker.waitForEspresso();

        int lastScreenPos = getRecyclerView().getChildCount() - 1;
        AnvilTestLocker playLocker = new AnvilTestLocker(getGifView(lastScreenPos));

        onView(withRecyclerPos(lastScreenPos)).perform(click());

        playLocker.registerIdlingResource();

        onView(withRecyclerPos(lastScreenPos)).check(matches(withPlayingGifDrawable()));

        playLocker.unregisterIdlingResource();
    }

    @Test
    public void whenScrollUpAndPlayFirstItemTest() {
        int gifListSize = 5;
        mHelper.dispatchFakeAppState(buildAppState(createFakeDownloadedGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        AnvilTestLocker playLocker = new AnvilTestLocker(getGifView(0));

        AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedView());

        onView(withId(getRecyclerView().getId())).perform(scrollToPosition(gifListSize - 1));

        scrollLocker.waitForEspresso();

        onView(withId(getRecyclerView().getId())).perform(actionOnItemAtPosition(0, click()));

        playLocker.registerIdlingResource();

        onView(withRecyclerPos(0)).check(matches(withPlayingGifDrawable()));

        playLocker.unregisterIdlingResource();
    }

    @Test
    public void whenListKeepOrderedAfterPlayTest() {
        int gifListSize = 20;
        mHelper.dispatchFakeAppState(buildAppState(createFakeDownloadedGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        int screenPosToClick = 0;
        for (int i = 0; i < gifListSize; i++) {
            List<String> gifsUuidBackup = getAllUuidFromGifsOnScreen();

            AnvilTestLocker playLocker = new AnvilTestLocker(getGifView(screenPosToClick));

            onView(withRecyclerPos(screenPosToClick)).perform(click());

            playLocker.registerIdlingResource();
            int gifsOnScreen = getRecyclerView().getChildCount();
            for (int j = 0; j < gifsOnScreen; j++) {
                onView(withRecyclerPos(j))
                        .check(matches(withEqualsGifUuid(gifsUuidBackup.get(j))));
            }
            playLocker.unregisterIdlingResource();

            if (i + gifsOnScreen < gifListSize) {
                AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedView());

                onView(withId(getRecyclerView().getId()))
                        .perform(scrollToPosition(i + gifsOnScreen));

                scrollLocker.waitForEspresso();
            }

            screenPosToClick = getRecyclerView().getChildCount() - 1;
        }
    }

    @Test
    public void whenDefaultGifsAreLoaded() {
        try {
            DB db = DBFactory.open(getTargetContext());
            db.destroy();
        } catch (Exception e) {
            Log.e("TEST", e.getLocalizedMessage(), e);
        }

        mHelper.dispatchFakeAppState(ImmutableAppState.builder().build());

        mHelper.replacePersistenceMiddleware(new DataManager(getTargetContext()));
        GifActionCreator.getInstance().setDispatcher(mock(DispatcherImpl.class));

        mActivityTestRule.launchActivity(new Intent());

        Map<String, Gif> expectedGifs = getAppStateDefault().getGifs();

        RecyclerTestLocker locker = new RecyclerTestLocker(getRecyclerView(), expectedGifs.size());
        locker.waitForEspresso();

        Map<Strings, Gif> gotGifs = ((GifsAdapter) getRecyclerView().getAdapter()).getGifs();
        assertEquals(expectedGifs, gotGifs);

        GifActionCreator.getInstance().setDispatcher(getFluxxan().getDispatcher());
    }

    @Test
    public void whenEndlessScrollTriggered() {
        int gifListSize = 10;
        Map<String, Gif> expectedGifs = createFakeDownloadedGifList(gifListSize);
        mHelper.dispatchFakeAppState(buildAppState(expectedGifs));

        mActivityTestRule.launchActivity(new Intent());

        Map<String, Gif> newGifs = createFakeDownloadedGifList(gifListSize);
        expectedGifs.putAll(newGifs);
        mHelper.replaceRestMiddleware(mHelper.mockDataManagerFetch(newGifs, false),
                getTargetContext());

        RecyclerTestLocker locker = new RecyclerTestLocker(getRecyclerView(), expectedGifs.size());
        onView(withId(getRecyclerView().getId()))
                .perform(scrollToPosition(gifListSize - 1));
        locker.waitForEspresso();

        Map<String, Gif> gotGifs = ((GifsAdapter) getRecyclerView().getAdapter()).getGifs();
        assertEquals(expectedGifs, gotGifs);
        assertFalse(getFluxxan().getState().getHasMoreGifs());
    }

    @Test
    public void whenEndlessScrollNotTriggered() {
        int gifListSize = 10;
        Map<String, Gif> expectedGifs = createFakeDownloadedGifList(gifListSize);
        AppState withHasMoreFalse = ImmutableAppState.builder()
                .from(buildAppState(expectedGifs))
                .hasMoreGifs(false).build();
        mHelper.dispatchFakeAppState(withHasMoreFalse);

        mActivityTestRule.launchActivity(new Intent());

        Map<String, Gif> newGifs = createFakeDownloadedGifList(gifListSize);
        expectedGifs.putAll(newGifs);
        mHelper.replaceRestMiddleware(mHelper.mockDataManagerFetch(newGifs, true),
                getTargetContext());

        AnvilTestLocker scrollLocker = new AnvilTestLocker(getFeedView());
        onView(withId(getRecyclerView().getId()))
                .perform(scrollToPosition(gifListSize - 1));
        scrollLocker.waitForEspresso();

        Map<String, Gif> gotGifs = ((GifsAdapter) getRecyclerView().getAdapter()).getGifs();
        assertNotSame(expectedGifs, gotGifs);
        assertFalse(getFluxxan().getState().getHasMoreGifs());
    }

    private List<String> getAllUuidFromGifsOnScreen() {
        List<String> uuids = new ArrayList<>();
        for (int i = 0; i < getRecyclerView().getChildCount(); i++) {
            uuids.add(getGifView(i).getGif().getUuid());
        }
        return uuids;
    }

    private Map<String, Gif> createFakeDownloadedGifList(int size) {
        Map<String, Gif> gifs = new LinkedHashMap<>();
        File fakeGifFile = createFakeGifFile();
        for (int i = 0; i < size; i++) {
            Gif gif = gifBuilderWithEmpty()
                    .title("gif" + i)
                    .status(Gif.Status.DOWNLOADED)
                    .path(fakeGifFile.getAbsolutePath())
                    .build();
            gifs.put(gif.getUuid(), gif);
        }

        return gifs;
    }

    //Get item at screen position
    private Matcher<View> withRecyclerPos(int pos) {
        return new RecyclerViewMatcher(getRecyclerView().getId()).atPosition(pos);
    }

    private FeedView getFeedView() {
        return (FeedView) getActivity().findViewById(R.id.feed);
    }

    private RecyclerView getRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) getFeedView().getChildAt(0);
        recyclerView.setId(generateViewId());
        return recyclerView;
    }

    private GifView getGifView(int screenPos) {
        FrameLayout frameLayout = (FrameLayout) getRecyclerView().getChildAt(screenPos);
        return (GifView) frameLayout.getChildAt(0);
    }

    private GifListActivity getActivity() {
        return mActivityTestRule.getActivity();
    }
}
