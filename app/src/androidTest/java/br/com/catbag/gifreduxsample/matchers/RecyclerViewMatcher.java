package br.com.catbag.gifreduxsample.matchers;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created on 5/10/15 by dannyroa(https://github.com/dannyroa)
 * from https://github.com/dannyroa/espresso-samples
 */
public class RecyclerViewMatcher {
    private final int mRecyclerViewId;

    public RecyclerViewMatcher(int recyclerViewId) {
        this.mRecyclerViewId = recyclerViewId;
    }

    public Matcher<View> atPosition(final int position) {
        return atPositionOnView(position, -1);
    }

    public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

        return new TypeSafeMatcher<View>() {
            private Resources mResources = null;
            private View mChildView;

            public void describeTo(Description description) {
                String idDescription = Integer.toString(mRecyclerViewId);
                if (this.mResources != null) {
                    try {
                        idDescription = this.mResources.getResourceName(mRecyclerViewId);
                    } catch (Resources.NotFoundException var4) {
                        idDescription = String.format("%s (resource name not found)",
                                mRecyclerViewId);
                    }
                }

                description.appendText("with id: " + idDescription);
            }

            public boolean matchesSafely(View view) {

                this.mResources = view.getResources();

                if (mChildView == null) {
                    RecyclerView recyclerView
                            = (RecyclerView) view.getRootView().findViewById(mRecyclerViewId);
                    if (recyclerView != null && recyclerView.getId() == mRecyclerViewId) {
                        mChildView = recyclerView.getChildAt(position);
                    } else {
                        return false;
                    }
                }

                if (targetViewId == -1) {
                    return view == mChildView;
                } else {
                    View targetView = mChildView.findViewById(targetViewId);
                    return view == targetView;
                }

            }
        };
    }
}