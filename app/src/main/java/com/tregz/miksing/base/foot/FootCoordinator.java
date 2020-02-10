package com.tregz.miksing.base.foot;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class FootCoordinator <V extends View> extends CoordinatorLayout.Behavior<V> {

    private String TAG = FootCoordinator.class.getSimpleName();

    public FootCoordinator() {
    }

    public FootCoordinator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            @NonNull View directTargetChild,
            @NonNull View target,
            int axes,
            int type
    ) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull V child,
            @NonNull View target,
            int dx,
            int dy,
            @NonNull int[] consumed,
            int type
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        child.setTranslationY(max((-10f), min(child.getHeight(), child.getTranslationY() - dy)));
    }

    @Override
    public void onNestedScroll(
            @NonNull CoordinatorLayout layout,
            @NonNull V child,
            @NonNull View target,
            int dx0,
            int dy0,
            int dx1,
            int dy1,
            int type,
            @NonNull int[] consumed
    ) {
        super.onNestedScroll(layout, child, target, dx0, dy0, dx1, dy1, type, consumed);
        Log.d(TAG, "Child? " + child.getClass().getSimpleName());
        if (child instanceof FloatingActionButton) {
            if (child.getTranslationY() > 10) child.setVisibility(View.GONE);
            else child.setVisibility(View.VISIBLE);
        }
    }
}
