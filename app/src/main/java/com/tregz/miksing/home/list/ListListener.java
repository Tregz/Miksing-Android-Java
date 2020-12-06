package com.tregz.miksing.home.list;

public interface ListListener {

    void onGestureClear(final int destination);

    boolean onItemMoved(int from, int destination);

    void onItemSwipe(int position, int direction);

}
