package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyMedia;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResult;
import shared.TestBase;

import static junit.framework.Assert.assertEquals;

/**
 * Created by niltonvasques on 11/3/16.
 */

public class RiffsyResultTest extends TestBase{
  private List<RiffsyMedia> medias = new ArrayList<>();
  private RiffsyResult sut = new RiffsyResult.Builder().media(medias).title(STRING_UNIQUE).build();

  @Test
  public void testGetRiffsyMedia() {
    assertEquals(sut.media(), medias);
  }

  @Test
  public void testSetRiffsyMedia() {
    final List<RiffsyMedia> medias = new ArrayList<>();

    sut = sut.newBuilder().media(medias).build();

    assertEquals(sut.media(), medias);
  }

  @Test
  public void testGetTitle() {
    assertEquals(sut.title(), STRING_UNIQUE);
  }

  @Test
  public void testSetTitle() {
    sut = sut.newBuilder().title(STRING_UNIQUE2).build();

    assertEquals(sut.title(), STRING_UNIQUE2);
  }
}
