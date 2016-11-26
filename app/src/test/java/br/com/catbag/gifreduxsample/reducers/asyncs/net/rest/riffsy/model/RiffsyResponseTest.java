package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyResponse;
import br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model.RiffsyResult;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.DELTA;
import static shared.TestHelper.FLOAT_RANDOM;

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

public class RiffsyResponseTest {
    private List<RiffsyResult> mResults = new ArrayList<>();
    private RiffsyResponse mSut = new RiffsyResponse.Builder()
            .results(mResults)
            .next(FLOAT_RANDOM)
            .build();

    @Test
    public void testGetRiffsyResults() {
        assertEquals(mSut.results(), mResults);
    }

    @Test public void testSetRiffsyResults() {
        final List<RiffsyResult> expected = new ArrayList<>();

        mSut = mSut.newBuilder().results(expected).build();

        assertEquals(mSut.results(), expected);
    }

    @Test
    public void testGetNext() {
        assertEquals(mSut.next(), FLOAT_RANDOM, DELTA);
    }

    @Test
    public void testSetNext() {
        mSut = mSut.newBuilder().next(FLOAT_RANDOM + 1).build();

        assertEquals(mSut.next(), FLOAT_RANDOM + 1, DELTA);
    }
}
