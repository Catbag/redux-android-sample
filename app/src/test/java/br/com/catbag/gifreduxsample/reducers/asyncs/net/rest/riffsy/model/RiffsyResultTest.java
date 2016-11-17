package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyMedia;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResult;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.STRING_UNIQUE;
import static shared.TestHelper.STRING_UNIQUE_2;

/**
 * Created by niltonvasques on 11/3/16.
 */

public class RiffsyResultTest {
  private List<RiffsyMedia> mMedia = new ArrayList<>();
  private RiffsyResult mSut = new RiffsyResult.Builder().media(mMedia).title(STRING_UNIQUE).build();

  @Test
  public void testGetRiffsyMedia() {
    assertEquals(mSut.media(), mMedia);
  }

  @Test
  public void testSetRiffsyMedia() {
    final List<RiffsyMedia> medias = new ArrayList<>();

    mSut = mSut.newBuilder().media(medias).build();

    assertEquals(mSut.media(), medias);
  }

  @Test
  public void testGetTitle() {
    assertEquals(mSut.title(), STRING_UNIQUE);
  }

  @Test
  public void testSetTitle() {
    mSut = mSut.newBuilder().title(STRING_UNIQUE_2).build();

    assertEquals(mSut.title(), STRING_UNIQUE_2);
  }
}
