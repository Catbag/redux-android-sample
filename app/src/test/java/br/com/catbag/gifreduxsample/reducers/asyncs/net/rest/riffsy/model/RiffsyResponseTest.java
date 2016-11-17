package br.com.catbag.gifreduxsample.reducers.asyncs.net.rest.riffsy.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResponse;
import br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model.RiffsyResult;

import static junit.framework.Assert.assertEquals;
import static shared.TestHelper.DELTA;
import static shared.TestHelper.FLOAT_RANDOM;

/**
 * Created by niltonvasques on 11/3/16.
 */

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
