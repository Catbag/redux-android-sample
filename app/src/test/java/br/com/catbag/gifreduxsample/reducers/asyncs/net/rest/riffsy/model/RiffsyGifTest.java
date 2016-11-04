package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyGif;
import shared.TestBase;

import static junit.framework.Assert.assertEquals;

/**
 * Created by niltonvasques on 11/3/16.
 */
public final class RiffsyGifTest extends TestBase {
    private RiffsyGif sut = new RiffsyGif.Builder().url(STRING_UNIQUE).preview(STRING_UNIQUE_2).build();

    @Test
    public void testGetUrl() {
        assertEquals(sut.url(), STRING_UNIQUE);
    }

    @Test
    public void testSetUrl() {
        sut = sut.newBuilder().url(STRING_UNIQUE_2).build();

        assertEquals(sut.url(), STRING_UNIQUE_2);
    }

    @Test
    public void testGetPreview() {
        assertEquals(sut.preview(), STRING_UNIQUE_2);
    }

    @Test
    public void testSetPreview() {
        sut = sut.newBuilder().preview(STRING_UNIQUE_3).build();

        assertEquals(sut.preview(), STRING_UNIQUE_3);
    }
}
