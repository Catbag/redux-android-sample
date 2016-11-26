package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyMedia;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyResult;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.STRING_UNIQUE;
import static shared.TestHelper.STRING_UNIQUE_2;

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
