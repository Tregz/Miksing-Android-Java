package com.tregz.miksing.home;

import androidx.appcompat.widget.SearchView;

class HomeSearch implements SearchView.OnQueryTextListener {

    private HomeView view;

    HomeSearch(HomeView view, SearchView search) {
        search.setOnQueryTextListener(this);
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

}
