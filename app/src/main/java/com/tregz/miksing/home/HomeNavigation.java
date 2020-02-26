package com.tregz.miksing.home;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.tregz.miksing.R;
import com.tregz.miksing.arch.auth.AuthLogin;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.foot.FootNavigation;
import com.tregz.miksing.base.list.ListSorted;

public class HomeNavigation implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnCompleteListener<Void> {
    private final String TAG = HomeNavigation.class.getSimpleName();

    private final int RIGHT = HomeActivity.Drawer.RIGHT.ordinal();
    private final int START = HomeActivity.Drawer.START.ordinal();
    private String login, logout;
    private boolean initialized = false;
    private HomeView view;
    private DrawerLayout layout;
    private NavigationView[] drawers;

    HomeNavigation(
            @NonNull HomeView view,
            @NonNull FootNavigation bottom,
            @NonNull DrawerLayout layout,
            @NonNull NavigationView[] drawers
    ) {
        this.view = view;
        this.drawers = drawers;
        this.layout = layout;
        layout.addDrawerListener(this);
        for (NavigationView drawer : drawers) drawer.setNavigationItemSelectedListener(this);
        bottom.setOnNavigationItemSelectedListener(this);
        login = layout.getContext().getString(R.string.nav_login_title);
        logout = layout.getContext().getString(R.string.nav_logout_title);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        PrefShared.getInstance(layout.getContext()).setUsername("");
        PrefShared.getInstance(layout.getContext()).setEmail("");
        update();
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        // do nothing
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        if (drawerView.getId() == drawers[RIGHT].getId()) update();
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        // do nothing
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (!initialized) {
            initialized = true;
            headerHeight(RIGHT);
            headerHeight(START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");
        switch (item.getItemId()) {
            case R.id.nav_login:
                if (!AuthUtil.logged()) new AuthLogin(view);
                else AuthUI.getInstance().signOut(layout.getContext()).addOnCompleteListener(this);
                break;
            case R.id.sort_alpha:
                ListSorted.comparator = ListSorted.Order.ALPHA;
                view.sort();
                break;
            case R.id.sort_digit:
                ListSorted.comparator = ListSorted.Order.DIGIT;
                view.sort();
                break;
            case R.id.sort_fresh:
                ListSorted.comparator = ListSorted.Order.FRESH;
                view.sort();
                break;
        }
        return false;
    }

    void toggle(int gravity) {
        if (layout.isDrawerOpen(gravity)) layout.closeDrawer(gravity);
        else layout.openDrawer(gravity);
    }

    void update() {
        view.userFragment().update();
        MenuItem item = drawers[RIGHT].getMenu().getItem(0);
        if (AuthUtil.logged()) {
            if (item.getTitle() != logout) {
                item.setIcon(R.drawable.ic_exit);
                item.setTitle(logout);
            }
        } else {
            if (item.getTitle() != login) {
                item.setIcon(R.drawable.ic_key);
                item.setTitle(login);
            }
        }
    }

    /* Proportional height of drawer's header */
    private void headerHeight(int position) {
        TypedValue tv = new TypedValue();
        Context context = drawers[position].getContext();
        int size = android.R.attr.actionBarSize;
        if (context.getTheme().resolveAttribute(size, tv, true)) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            View header = drawers[position].getHeaderView(0);
            ViewGroup.LayoutParams params = header.getLayoutParams();
            int height = ((Activity) context).getWindow().getDecorView().getHeight();
            params.height = height - TypedValue.complexToDimensionPixelSize(tv.data, dm);
            header.setLayoutParams(params);
        }
    }
}
