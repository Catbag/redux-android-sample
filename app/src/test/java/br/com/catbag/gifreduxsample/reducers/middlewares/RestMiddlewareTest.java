package br.com.catbag.gifreduxsample.reducers.middlewares;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.Middleware;
import com.umaplay.fluxxan.impl.BaseMiddleware;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import br.com.catbag.gifreduxsample.BuildConfig;
import br.com.catbag.gifreduxsample.actions.PayloadParams;
import br.com.catbag.gifreduxsample.asyncs.data.DataManager;
import br.com.catbag.gifreduxsample.asyncs.net.downloader.FileDownloader;
import br.com.catbag.gifreduxsample.middlewares.RestMiddleware;
import br.com.catbag.gifreduxsample.models.AppState;
import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableAppState;
import shared.ReduxBaseTest;
import shared.TestHelper;

import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_FAILURE;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_START;
import static br.com.catbag.gifreduxsample.actions.GifActionCreator.GIF_DOWNLOAD_SUCCESS;
import static br.com.catbag.gifreduxsample.actions.GifListActionCreator.GIF_LIST_FETCHING;
import static br.com.catbag.gifreduxsample.actions.GifListActionCreator.GIF_LIST_UPDATED;
import static br.com.catbag.gifreduxsample.asyncs.data.DataManager.GifListLoadListener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;
import static shared.TestHelper.buildGif;

// Roboeletric still not supports API 24 stuffs
@Config(sdk = 23, constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class RestMiddlewareTest extends ReduxBaseTest {

    public static final boolean EXPECTED_HAS_MORE = false;
    public static final Map EXPECTED_GIFS = new LinkedHashMap<>();

    private Fluxxan<AppState> mFluxxan;
    private CountDownLatch mSignal;
    private Action mLastAction;

    private Middleware mInterceptMiddleware = new BaseMiddleware() {
        @Override
        public void intercept(Object o, Action action) throws Exception {
            mLastAction = action;
            mSignal.countDown();
        }
    };

    public RestMiddlewareTest() {
        mFluxxan = new Fluxxan<>(ImmutableAppState.builder().build());
        mHelper = new TestHelper(mFluxxan);
        mFluxxan.start();
    }

    @Before
    public void setup() {
        mSignal = new CountDownLatch(1);
        mFluxxan.getDispatcher().registerMiddleware(mInterceptMiddleware);
    }

    @After
    @Override
    public void cleanup() {
        super.cleanup();
        mFluxxan.stop();
        mFluxxan.getDispatcher().unregisterMiddleware(mInterceptMiddleware.getClass());
    }

    @Test
    public void whenInterceptListFetching() throws Exception {
        RestMiddleware middleware = new RestMiddleware(application, mockDataManager(),
                any(FileDownloader.class));
        middleware.setDispatcher(mFluxxan.getDispatcher());
        middleware.intercept(mFluxxan.getState(), new Action(GIF_LIST_FETCHING));
        mSignal.await();

        assertEquals(GIF_LIST_UPDATED, mLastAction.Type);
        assertEquals(mLastAction.Payload.getClass(), HashMap.class);
        Map params = (Map) mLastAction.Payload;
        assertEquals(params.get(PayloadParams.PARAM_HAS_MORE), EXPECTED_HAS_MORE);
        Map<String, Gif> gotGifs = (Map) params.get(PayloadParams.PARAM_GIFS);
        assertEquals(gotGifs.size(), EXPECTED_GIFS.size());
    }

    @Test
    public void whenInterceptListFetchingAndDontHasMoreGifs() throws Exception {
        AppState state = ImmutableAppState.builder().hasMoreGifs(false).build();
        DataManager dataManager = mockDataManager();
        RestMiddleware middleware = new RestMiddleware(application, dataManager, null);
        middleware.setDispatcher(mFluxxan.getDispatcher());
        middleware.intercept(state, new Action(GIF_LIST_FETCHING));

        verify(dataManager, never()).fetchGifs(any(GifListLoadListener.class));
    }

    @Test
    public void whenInterceptNonUsefulActions() throws Exception {
        AppState state = ImmutableAppState.builder().hasMoreGifs(false).build();
        DataManager dataManager = mockDataManager();
        FileDownloader fileDownloader = mock(FileDownloader.class);
        RestMiddleware middleware = new RestMiddleware(application,
                dataManager, fileDownloader);
        middleware.setDispatcher(mFluxxan.getDispatcher());
        middleware.intercept(state, new Action("RANDOM_ACTION"));

        verify(dataManager, never()).fetchGifs(any(GifListLoadListener.class));
        verify(fileDownloader, never()).download(anyString(), anyString(),
                any(FileDownloader.SuccessDownloadListener.class),
                any(FileDownloader.FailureDownloadListener.class));
    }

    @Test
    public void whenInterceptGifDownloadStartWithSuccess() throws Exception {
        Gif gif = buildGif();
        AppState state = TestHelper.buildAppState(gif);
        RestMiddleware middleware = new RestMiddleware(application,
                mockDataManager(), mockFileDownloaderWithSuccess());
        middleware.setDispatcher(mFluxxan.getDispatcher());
        middleware.intercept(state, new Action(GIF_DOWNLOAD_START, gif.getUuid()));
        mSignal.await();

        assertEquals(GIF_DOWNLOAD_SUCCESS, mLastAction.Type);
        assertEquals(mLastAction.Payload.getClass(), HashMap.class);
        Map params = (Map) mLastAction.Payload;
        assertTrue(params.containsKey(PayloadParams.PARAM_PATH));
        assertTrue(params.containsKey(PayloadParams.PARAM_UUID));
    }

    @Test
    public void whenInterceptGifDownloadStartWithFailure() throws Exception {
        Gif gif = buildGif();
        AppState state = TestHelper.buildAppState(gif);
        Middleware middleware = new RestMiddleware(application,
                mockDataManager(), mockFileDownloaderFailure());
        middleware.setDispatcher(mFluxxan.getDispatcher());

        middleware.intercept(state, new Action(GIF_DOWNLOAD_START, gif.getUuid()));
        mSignal.await();

        assertEquals(GIF_DOWNLOAD_FAILURE, mLastAction.Type);
        assertEquals(mLastAction.Payload, gif.getUuid());
    }

    private DataManager mockDataManager() {
        DataManager dataManager = mock(DataManager.class);
        ArgumentCaptor<GifListLoadListener> listenerCaptor
                = ArgumentCaptor.forClass(GifListLoadListener.class);
        doAnswer((invocationOnMock) -> {
            new Thread(() -> {
                // The fluxxan don't let we dispatch when it's dispatching
                while (mFluxxan.getDispatcher().isDispatching()) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Logger.error(e.getMessage(), e);
                    }
                }
                listenerCaptor.getValue().onLoaded(EXPECTED_GIFS, EXPECTED_HAS_MORE);
            }).start();
            return null;
        }).when(dataManager).fetchGifs(listenerCaptor.capture());
        return dataManager;
    }

    private FileDownloader mockFileDownloaderWithSuccess() {
        FileDownloader downloader = mock(FileDownloader.class);
        ArgumentCaptor<FileDownloader.SuccessDownloadListener> successCaptor
                = ArgumentCaptor.forClass(FileDownloader.SuccessDownloadListener.class);
        doAnswer(invocation -> {
            successCaptor.getValue().onSuccess();
            return null;
        }).when(downloader).download(anyString(), anyString(), successCaptor.capture(),
                any(FileDownloader.FailureDownloadListener.class));
        return downloader;
    }

    private FileDownloader mockFileDownloaderFailure() {
        FileDownloader downloader = mock(FileDownloader.class);
        ArgumentCaptor<FileDownloader.FailureDownloadListener> failureCaptor
                = ArgumentCaptor.forClass(FileDownloader.FailureDownloadListener.class);
        doAnswer(invocation -> {
            failureCaptor.getValue().onFailure(any(Exception.class));
            return null;
        }).when(downloader).download(anyString(), anyString(),
                any(FileDownloader.SuccessDownloadListener.class),
                failureCaptor.capture());
        return downloader;
    }

}
