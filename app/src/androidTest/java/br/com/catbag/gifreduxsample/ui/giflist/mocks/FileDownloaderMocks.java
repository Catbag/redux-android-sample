package br.com.catbag.gifreduxsample.ui.giflist.mocks;

import br.com.catbag.gifreduxsample.actions.GifActionCreator;
import br.com.catbag.gifreduxsample.asyncs.restservice.FileDownloader;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by niltonvasques on 10/20/16.
 */

public class FileDownloaderMocks {

    private static FileDownloader.FailureDownloadListener sFailureListener;
    private static FileDownloader.StartDownloadListener sStartListener;
    private static FileDownloader sDownloader;

    public static void mockFileDownloaderToDownloadInfinite() {
        sDownloader = mock(FileDownloader.class);

        doAnswer( (invocation) -> {
            Object[] args = invocation.getArguments();
            Object mock = invocation.getMock();
            sStartListener = (FileDownloader.StartDownloadListener)args[0];
            return mock;
        }).when(sDownloader).onStart(any(FileDownloader.StartDownloadListener.class));

        doAnswer( (invocation) -> {
            sStartListener.onStarted();
            return null;
        }).when(sDownloader).download(anyString(), anyString());

        doReturn(sDownloader).when(sDownloader)
                .onFailure(any(FileDownloader.FailureDownloadListener.class));
        doReturn(sDownloader).when(sDownloader)
                .onSuccess(any(FileDownloader.SuccessDownloadListener.class));

        GifActionCreator.getInstance().setFileDownloader(sDownloader);
    }

    public static void mockFileDownloaderToAlwaysFail() {
        sDownloader = mock(FileDownloader.class);
        doAnswer( (invocation) -> {
            sFailureListener.onFailure(new Exception("Download error"));
            return null;
        }).when(sDownloader).download(anyString(), anyString());

        doAnswer( (invocation) -> {
            Object[] args = invocation.getArguments();
            Object mock = invocation.getMock();
            sFailureListener = (FileDownloader.FailureDownloadListener)args[0];
            return mock;
        }).when(sDownloader).onFailure(any(FileDownloader.FailureDownloadListener.class));
        doReturn(sDownloader).when(sDownloader).onSuccess(any(FileDownloader.SuccessDownloadListener.class));
        doReturn(sDownloader).when(sDownloader).onStart(any(FileDownloader.StartDownloadListener.class));

        GifActionCreator.getInstance().setFileDownloader(sDownloader);
    }

    public static void removeMockInFileDownloader(){
        GifActionCreator.getInstance().setFileDownloader(new FileDownloader());
    }
}
