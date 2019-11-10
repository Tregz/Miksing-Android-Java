package com.tregz.miksing.home;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.tregz.miksing.data.item.Item;

public interface HomeView {

    void onClearItemDetails();

    void onFillItemDetails(Item item);

    void startActivityForResult(Intent intent, int requestCode);

    void onSaved();

    void onSaveItem();

    void commit(int container, Fragment fragment, String tag);

    void remove(String tag);

}
