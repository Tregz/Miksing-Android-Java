package com.tregz.miksing.home.list;

public interface ListView {

    void onGestureClear(final int from, final int destination);

    void onItemMoved(int from, int destination);

    void onItemSwipe(int position, int direction);

    void ordered();

}
