package com.tregz.miksing.home.list;

public interface ListView {

    void onItemMoved(int from, int destination);

    void onGestureClear(final int position);
}
