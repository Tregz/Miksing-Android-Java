package com.tregz.miksing.base.foot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.snackbar.Snackbar;

public class FootBehavior<V extends View> extends HideBottomViewOnScrollBehavior<V> {
    private final String TAG = FootBehavior.class.getSimpleName();

    public FootBehavior() {
    }

    public FootBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(
            @NonNull CoordinatorLayout parent,
            @NonNull V child,
            @NonNull View target
    ) {
        if (target instanceof Snackbar.SnackbarLayout)
            if (target.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams params;
                params = (CoordinatorLayout.LayoutParams) target.getLayoutParams();
                params.setAnchorId(child.getId());
                params.anchorGravity = Gravity.TOP;
                params.gravity = Gravity.TOP;
                target.setLayoutParams(params);
            }
        return super.layoutDependsOn(parent, child, target);
    }

    @Override
    public void onNestedScroll(
            CoordinatorLayout parent,
            @NonNull V child,
            @NonNull View target,
            int dx0,
            int dy0,
            int dx1,
            int dy1,
            int type,
            @NonNull int[] consumed
    ) {
        if (child instanceof FootNavigation && ((FootNavigation) child).shown) {
            if (dy0 > dy1) slideUp(child);
            if (dy0 < dy1) slideDown(child);
        }
    }
}