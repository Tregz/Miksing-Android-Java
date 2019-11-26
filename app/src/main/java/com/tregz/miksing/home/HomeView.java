package com.tregz.miksing.home;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tregz.miksing.R;
import com.tregz.miksing.data.item.Item;
import com.tregz.miksing.data.item.work.song.Song;
import com.tregz.miksing.home.user.UserFragment;
import com.tregz.miksing.home.user.UserMap;

import java.util.List;

public interface HomeView {

    void startActivityForResult(Intent intent, int requestCode);

    void onClearItemDetails();

    void onFillItemDetails(Item item);

    void onSaved();

    void onSaveItem();

    void sort();

    void search(String query);

    void search(boolean focused);

    UserFragment userFragment();

    UserMap areaFragment();

}
