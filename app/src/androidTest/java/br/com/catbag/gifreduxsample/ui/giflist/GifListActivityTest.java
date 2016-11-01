package br.com.catbag.gifreduxsample.ui.giflist;


import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.FrameLayout;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.Fluxxan;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.R;
import br.com.catbag.gifreduxsample.actions.GifListActionCreator;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.helpers.AppStateHelper;
import br.com.catbag.gifreduxsample.idlings.UiTestLocker;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import br.com.catbag.gifreduxsample.models.ImmutableGif;
import br.com.catbag.gifreduxsample.ui.GifListActivity;
import br.com.catbag.gifreduxsample.ui.components.FeedComponent;
import br.com.catbag.gifreduxsample.ui.components.GifComponent;
import shared.FakeReducer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withBGColor;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withGifDrawable;
import static br.com.catbag.gifreduxsample.matchers.Matchers.withPlayingGifDrawable;
import static br.com.catbag.gifreduxsample.utils.FileUtils.createFakeGifFile;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GifListActivityTest {

    @Rule
    public ActivityTestRule<GifListActivity> mActivityTestRule
            = new ActivityTestRule<>(GifListActivity.class, false, false);

    @Before
    public void setUp(){
        GifListActionCreator.getInstance().setDataManager(mock(DataManager.class));
        getFluxxan().registerReducer(new FakeReducer());
    }

    @After
    public void cleanUp(){
        getFluxxan().unregisterReducer(FakeReducer.class);
        GifListActionCreator.getInstance().setDataManager(new DataManager());
    }

    @Test
    public void whenGifIsDownloadingTest() {
        Gif gif = gifBuilder()
                .status(Gif.Status.DOWNLOADING)
                .build();

        dispatchFakeReduceAction(createStateFromGif(gif));

        /** On activity create the Fluxxan calls onStateChanged synchronously.
         * So isn't required to use idling resources or others wait conditions **/
        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.gif_loading)).check(matches(isDisplayed()));
    }

    @Test
    public void whenGifIsDownloadedTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = gifBuilder()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.DOWNLOADED)
                .build();

        dispatchFakeReduceAction(createStateFromGif(gif));

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.gif_loading)).check(matches(not(isDisplayed())));

        onView(withId(R.id.gif_image)).check(matches(isDisplayed()));

        onView(withId(R.id.gif_image)).check(matches(withGifDrawable()));

        onView(withId(R.id.gif_image)).check(matches(not(withPlayingGifDrawable())));
    }

    @Test
    public void whenGifIsNotWatchedTest() {
        Gif gif = gifBuilder()
                .status(Gif.Status.DOWNLOADED)
                .watched(false)
                .build();

        dispatchFakeReduceAction(createStateFromGif(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.notWatched);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenGifIsWatchedTest() {
        Gif gif = gifBuilder()
                .status(Gif.Status.DOWNLOADED)
                .watched(true)
                .build();

        dispatchFakeReduceAction(createStateFromGif(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.watched);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenPlayGifTest() {
        File fakeGifFile = createFakeGifFile();
        Gif gif = gifBuilder()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.DOWNLOADED)
                .build();

        dispatchFakeReduceAction(createStateFromGif(gif));

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
        Gif gif = gifBuilder()
                .path(fakeGifFile.getAbsolutePath())
                .status(Gif.Status.LOOPING)
                .build();

        dispatchFakeReduceAction(createStateFromGif(gif));

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
        Gif gif = gifBuilder()
                .status(Gif.Status.DOWNLOAD_FAILED)
                .build();

        dispatchFakeReduceAction(createStateFromGif(gif));

        mActivityTestRule.launchActivity(new Intent());

        int expectedColor
                = ContextCompat.getColor(mActivityTestRule.getActivity(), R.color.error);

        onView(withId(R.id.gif_item))
                .check(matches(withBGColor(expectedColor)));
    }

    @Test
    public void whenLoadGifListWithMultipleItemsTest() {
        int gifListSize = 5;
        dispatchFakeReduceAction(createStateFromGifs(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        FeedComponent feed = (FeedComponent) getActivity().findViewById(R.id.feed);
        RecyclerView recyclerView = (RecyclerView) feed.getChildAt(0);
        recyclerView.setId(View.generateViewId());

        for (int i = 0; i < gifListSize; i++) {
            onView(withId(recyclerView.getId())).perform(scrollToPosition(i));
        }

        assertEquals(gifListSize, recyclerView.getAdapter().getItemCount());
        //Testa repetições, elemento e estados
    }

    @Test
    public void whenScrollDownAndPlayLastItemTest() {
        int gifListSize = 5;
        dispatchFakeReduceAction(createStateFromGifs(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        FeedComponent feed = (FeedComponent) getActivity().findViewById(R.id.feed);
        RecyclerView recyclerView = (RecyclerView) feed.getChildAt(0);
        recyclerView.setId(View.generateViewId());

        int lastItemPositionOnScreen = recyclerView.getChildCount()-1;
        UiTestLocker locker = new UiTestLocker(getGifComponent(lastItemPositionOnScreen));

        onView(withId(recyclerView.getId()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(gifListSize-1, click()));

        locker.registerIdlingResource();

        int lastViewId = View.generateViewId();
        FrameLayout frame = ((FrameLayout)recyclerView.getChildAt(lastItemPositionOnScreen));
        View lastView = frame.findViewById(R.id.gif_image);
        lastView.setId(lastViewId);

        onView(withId(lastViewId))
                .check(matches(withPlayingGifDrawable()));

        lastView.setId(R.id.gif_image);
        locker.unregisterIdlingResource();
    }

    @Test
    public void whenScrollUpAndPlayFirstItemTest() {
        int gifListSize = 5;
        dispatchFakeReduceAction(createStateFromGifs(createFakeGifList(gifListSize)));

        mActivityTestRule.launchActivity(new Intent());

        FeedComponent feed = (FeedComponent) getActivity().findViewById(R.id.feed);
        RecyclerView recyclerView = (RecyclerView) feed.getChildAt(0);
        recyclerView.setId(View.generateViewId());

        onView(withId(recyclerView.getId())).perform(scrollToPosition(gifListSize-1));

        UiTestLocker locker = new UiTestLocker(getGifComponent(0));

        onView(withId(recyclerView.getId()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        locker.registerIdlingResource();

        int firstViewId = View.generateViewId();
        FrameLayout frame = ((FrameLayout)recyclerView.getChildAt(0));
        View firstView = frame.findViewById(R.id.gif_image);
        firstView.setId(firstViewId);

        onView(withId(firstViewId))
                .check(matches(withPlayingGifDrawable()));

        firstView.setId(R.id.gif_image);
        locker.unregisterIdlingResource();
    }

    private void dispatchFakeReduceAction(AppState state) {
        getFluxxan().getDispatcher()
                .dispatch(new Action(FakeReducer.FAKE_REDUCE_ACTION, state));
    }

    private GifListActivity getActivity(){
        return mActivityTestRule.getActivity();
    }

    private Fluxxan<AppState> getFluxxan(){
        return MyApp.getFluxxan();
    }

    private List<Gif> createFakeGifList(int size) {
        List<Gif> gifs = new ArrayList<>();
        File fakeGifFile = createFakeGifFile();
        for (int i = 0; i < size; i++) {
            Gif gif = gifBuilder()
                    .title("gif"+i)
                    .status(Gif.Status.DOWNLOADED)
                    .path(fakeGifFile.getAbsolutePath())
                    .build();
            gifs.add(gif);
        }

        return gifs;
    }

    private GifComponent getGifComponent(int pos) {
        FeedComponent feed = (FeedComponent) getActivity().findViewById(R.id.feed);
        RecyclerView recyclerView = (RecyclerView) feed.getChildAt(0);
        FrameLayout frameLayout = (FrameLayout) recyclerView.getChildAt(pos);
        return ((GifComponent) frameLayout.getChildAt(0));
    }

    private AppState createStateFromGif(Gif gif) {
        return ImmutableAppState.builder().putGifs(gif.getUuid(), gif).build();
    }

    private AppState createStateFromGifs(List<Gif> gifs) {
        return ImmutableAppState.builder().putAllGifs(AppStateHelper.gifListToMap(gifs)).build();
    }

    private ImmutableGif.Builder gifBuilder() {
        return ImmutableGif.builder().uuid(UUID.randomUUID().toString())
                .title("")
                .url("");
    }
}
