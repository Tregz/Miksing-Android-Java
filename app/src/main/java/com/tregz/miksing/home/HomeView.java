package com.tregz.miksing.home;

import android.content.Intent;

import com.tregz.miksing.core.play.PlayWeb;
import com.tregz.miksing.data.DataObject;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.home.user.UserFragment;
import com.tregz.miksing.home.user.UserMap;

import java.util.List;

public interface HomeView {

    PlayWeb getWebView();

    UserFragment userFragment();

    UserMap areaFragment();

    void next(String id);

    void onClearItemDetails();

    void onClearPlaylist();

    void onFillItemDetails(DataObject item);

    void onHttpRequestResult(List<Song> list);

    void onPastePlaylist(String name);

    void onSaved();

    void onSaveItem(String name);

    void onWreck(Tube tube);

    void onDrawerStartOpened();

    void sort();

    void search(String query);

    void search(boolean focused);

    void startActivityForResult(Intent intent, int requestCode);

}
