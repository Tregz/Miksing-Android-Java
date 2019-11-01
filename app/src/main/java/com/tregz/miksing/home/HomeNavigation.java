package com.tregz.miksing.home;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeNavigation implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = HomeNavigation.class.getSimpleName();

    private DrawerLayout drawer;

    HomeNavigation(DrawerLayout drawer, NavigationView start) {
        this.drawer = drawer;
        drawer.addDrawerListener(this);
        start.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        // "not implemented yet"
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        // "not implemented yet"
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        // "not implemented yet"
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        // "not implemented yet"
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");
        return false;
    }

    void toggle(int gravity) {
        if (drawer.isDrawerOpen(gravity)) drawer.closeDrawer(gravity);
        else drawer.openDrawer(gravity);
    }
}
