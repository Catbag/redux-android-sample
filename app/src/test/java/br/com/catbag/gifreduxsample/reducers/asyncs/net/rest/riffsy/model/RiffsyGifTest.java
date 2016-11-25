package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyGif;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.STRING_UNIQUE;
import static shared.TestHelper.STRING_UNIQUE_2;
import static shared.TestHelper.STRING_UNIQUE_3;

/**
 * Created by niltonvasques on 11/3/16.
 */
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
