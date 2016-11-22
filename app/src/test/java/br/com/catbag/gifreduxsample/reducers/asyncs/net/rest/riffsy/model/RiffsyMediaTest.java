package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyGif;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyMedia;

import static junit.framework.Assert.assertEquals;

/**
 * Created by niltonvasques on 11/3/16.
 */

public class RiffsyMediaTest {
  private final RiffsyGif mGif = new RiffsyGif();
  private RiffsyMedia mSut = new RiffsyMedia.Builder().gif(mGif).build();

  @Test
  public void testGetGif() {
    assertEquals(mSut.gif(), mGif);
  }

  @Test
  public void testSetGif() {
    final RiffsyGif expected = new RiffsyGif();

    mSut = mSut.newBuilder().gif(expected).build();

    assertEquals(mSut.gif(), expected);
  }
}
