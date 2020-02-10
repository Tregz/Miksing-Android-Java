package com.tregz.miksing.base.foot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transformation.FabTransformationScrimBehavior;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class FootDialer extends FabTransformationScrimBehavior {

    //private String TAG = FootDialer.class.getSimpleName();

    public FootDialer() {
    }

    public FootDialer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof FloatingActionButton;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        for (int index = 0; index < ((ViewGroup)child).getChildCount(); ++index) {
            View nextChild = ((ViewGroup)child).getChildAt(index);
            if (dependency.getTranslationY() > 10) nextChild.setVisibility(View.GONE);
            else nextChild.setVisibility(View.VISIBLE);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child,
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
            @NonNull View child,
            @NonNull View target,
            int dx,
            int dy,
            @NonNull int[] consumed,
            int type
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        child.setTranslationY(max((0f), min(child.getHeight(), child.getTranslationY() - dy)));
    }
}
