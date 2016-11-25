package br.com.catbag.gifreduxsample.lockers;

import android.support.v7.widget.RecyclerView;

/**
 * Created by niltonvasques on 10/24/16.
 */

public class RecyclerTestLocker extends BaseTestLocker {

    private RecyclerView mRecycler;
    private int mExpectedItemsCount;

    public RecyclerTestLocker(RecyclerView recycler, int expectedItemsCount) {
        mRecycler = recycler;
        mExpectedItemsCount = expectedItemsCount;
    }

    @Override
    protected boolean isIdle() {
        return mRecycler.getAdapter().getItemCount() == mExpectedItemsCount;
    }
}