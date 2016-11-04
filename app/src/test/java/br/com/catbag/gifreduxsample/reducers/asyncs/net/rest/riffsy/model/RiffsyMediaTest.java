package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyGif;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyMedia;

import static junit.framework.Assert.assertEquals;

/**
 * Created by niltonvasques on 11/3/16.
 */

public class RiffsyMediaTest {
  private final RiffsyGif gif = new RiffsyGif();
  private RiffsyMedia sut = new RiffsyMedia.Builder().gif(gif).build();

  @Test
  public void testGetGif() {
    assertEquals(sut.gif(), gif);
  }

  @Test
  public void testSetGif() {
    final RiffsyGif expected = new RiffsyGif();

    sut = sut.newBuilder().gif(expected).build();

    assertEquals(sut.gif(), expected);
  }
}
