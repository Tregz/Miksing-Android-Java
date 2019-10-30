package com.tregz.miksing.home;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeNavigation implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    HomeNavigation(DrawerLayout drawer) {
        this.drawer = drawer;
        drawer.addDrawerListener(this);
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
        return false;
    }

    void toggle() {
        if (drawer.isDrawerOpen(GravityCompat.END)) drawer.closeDrawer(GravityCompat.END);
        else drawer.openDrawer(GravityCompat.END);
    }
}
