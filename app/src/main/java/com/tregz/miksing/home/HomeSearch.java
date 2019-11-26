package com.tregz.miksing.home;

import android.view.View;

import androidx.appcompat.widget.SearchView;

class HomeSearch implements SearchView.OnQueryTextListener, View.OnFocusChangeListener {

    private HomeView view;

    HomeSearch(HomeView view, SearchView search) {
        search.setOnQueryTextListener(this);
        search.setOnQueryTextFocusChangeListener(this);
        this.view = view;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        view.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        view.search(hasFocus);
    }
}
