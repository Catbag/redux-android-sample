package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyGif;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyMedia;

import static junit.framework.Assert.assertEquals;

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
