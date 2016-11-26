package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyGif;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.STRING_UNIQUE;
import static shared.TestHelper.STRING_UNIQUE_2;
import static shared.TestHelper.STRING_UNIQUE_3;

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
public final class RiffsyGifTest {
    private RiffsyGif mSut = new RiffsyGif.Builder()
            .url(STRING_UNIQUE)
            .preview(STRING_UNIQUE_2)
            .build();

    @Test
    public void testGetUrl() {
        assertEquals(mSut.url(), STRING_UNIQUE);
    }

    @Test
    public void testSetUrl() {
        mSut = mSut.newBuilder().url(STRING_UNIQUE_2).build();

        assertEquals(mSut.url(), STRING_UNIQUE_2);
    }

    @Test
    public void testGetPreview() {
        assertEquals(mSut.preview(), STRING_UNIQUE_2);
    }

    @Test
    public void testSetPreview() {
        mSut = mSut.newBuilder().preview(STRING_UNIQUE_3).build();

        assertEquals(mSut.preview(), STRING_UNIQUE_3);
    }
}
