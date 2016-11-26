package br.com.catbag.gifreduxsample.actions;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import br.com.catbag.gifreduxsample.MyApp;
import br.com.catbag.gifreduxsample.models.Gif;

/**
 Copyright 26/10/2016
 Felipe Piñeiro (fpbitencourt@gmail.com),
 Nilton Vasques (nilton.vasques@gmail.com) and
 Raul Abreu (raulccabreu@gmail.com)

 Be free to ask for help, email us!

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/

public final class GifActionCreator extends BaseActionCreator {
    public static final String GIF_PLAY = "GIF_PLAY";
    public static final String GIF_PAUSE = "GIF_PAUSE";
    public static final String GIF_DOWNLOAD_SUCCESS = "GIF_DOWNLOAD_SUCCESS";
    public static final String GIF_DOWNLOAD_FAILURE = "GIF_DOWNLOAD_FAILURE";
    public static final String GIF_DOWNLOAD_START = "GIF_DOWNLOAD_START";

    private static GifActionCreator sInstance;

    private GifActionCreator() {
        MyApp.getFluxxan().inject(this);
    }

    public static GifActionCreator getInstance() {
        if (sInstance == null) {
            sInstance = new GifActionCreator();
        }
        return sInstance;
    }

    public void gifDownloadStart(Gif gif) {
        dispatch(new Action(GIF_DOWNLOAD_START, gif.getUuid()));
    }

    public void gifClick(Gif gif) {
        if (gif.getStatus() == Gif.Status.DOWNLOADED || gif.getStatus() == Gif
                .Status.PAUSED) {
            gifPlay(gif.getUuid());
        } else if (gif.getStatus() == Gif.Status.LOOPING) {
            gifPause(gif.getUuid());
        }
    }

    private void gifPlay(String uuid) {
        dispatch(new Action(GIF_PLAY, uuid));
    }

    private void gifPause(String uuid) {
        dispatch(new Action(GIF_PAUSE, uuid));
    }
}