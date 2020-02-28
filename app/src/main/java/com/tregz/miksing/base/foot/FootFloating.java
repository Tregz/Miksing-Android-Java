package com.tregz.miksing.base.foot;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tregz.miksing.arch.pref.PrefShared;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class FootFloating extends FloatingActionButton.Behavior {

    //private final String TAG = FootFloating.class.getSimpleName();

    public FootFloating(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull FloatingActionButton child,
            @NonNull View directTargetChild,
            @NonNull View target,
            int axes,
            int type
    ) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull FloatingActionButton child,
            @NonNull View target,
            int dx0,
            int dy0,
            int dx1,
            int dy1,
            int type,
            @NonNull int[] consumed
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dx0, dy0, dx1, dy1, type, consumed);
        if (dy1 < 0 && child.getVisibility() == View.VISIBLE) {
            if (!child.isExpanded())
                child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        fab.setVisibility(View.INVISIBLE);
                    }
                });
        } else if (dy1 > 0 && child.getVisibility() != View.VISIBLE) {
            if (PrefShared.getInstance(child.getContext()).getUid() != null) child.show();
        }
    }
}
