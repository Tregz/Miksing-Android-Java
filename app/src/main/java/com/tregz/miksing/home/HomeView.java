package com.tregz.miksing.home;

import android.content.Intent;

import com.tregz.miksing.data.DataItem;
import com.tregz.miksing.home.user.UserFragment;
import com.tregz.miksing.home.user.UserMap;

public interface HomeView {

    void startActivityForResult(Intent intent, int requestCode);

    void onClearItemDetails();

    void onFillItemDetails(DataItem item);

    void onSaved();

    void onSaveItem();

    void sort();

    void search(String query);

    void search(boolean focused);

    UserFragment userFragment();

    UserMap areaFragment();

}
