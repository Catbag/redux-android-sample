package br.com.catbag.gifreduxsample.actions;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseActionCreator;

import br.com.catbag.gifreduxsample.MyApp;

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

public final class GifListActionCreator extends BaseActionCreator {

    public static final String GIF_LIST_UPDATED = "GIF_LIST_UPDATED";
    public static final String GIF_LIST_FETCHING = "GIF_LIST_FETCHING";

    private static GifListActionCreator sInstance;

    private GifListActionCreator() {
        MyApp.getFluxxan().inject(this);
    }

    public static GifListActionCreator getInstance() {
        if (sInstance == null) {
            sInstance = new GifListActionCreator();
        }
        return sInstance;
    }

    public void fetchGifs() {
        dispatch(new Action(GIF_LIST_FETCHING));
    }
}
