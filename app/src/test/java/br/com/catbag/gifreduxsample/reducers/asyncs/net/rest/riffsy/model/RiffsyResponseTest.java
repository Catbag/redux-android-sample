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
    private List<RiffsyResult> results = new ArrayList<>();
    private RiffsyResponse sut = new RiffsyResponse.Builder()
            .results(results)
            .next(FLOAT_RANDOM)
            .build();

    @Test
    public void testGetRiffsyResults() {
        assertEquals(sut.results(), results);
    }

    @Test public void testSetRiffsyResults() {
        final List<RiffsyResult> expected = new ArrayList<>();

        sut = sut.newBuilder().results(expected).build();

        assertEquals(sut.results(), expected);
    }

    @Test
    public void testGetNext() {
        assertEquals(sut.next(), FLOAT_RANDOM, DELTA);
    }

    @Test
    public void testSetNext() {
        sut = sut.newBuilder().next(FLOAT_RANDOM + 1).build();

        assertEquals(sut.next(), FLOAT_RANDOM + 1, DELTA);
    }
}
