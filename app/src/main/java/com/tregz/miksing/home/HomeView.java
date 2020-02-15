package com.tregz.miksing.home;

import android.content.Intent;
import android.webkit.WebView;

import com.tregz.miksing.core.play.PlayWeb;
import com.tregz.miksing.data.DataItem;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.home.user.UserFragment;
import com.tregz.miksing.home.user.UserMap;

import java.util.List;

public interface HomeView {

    void startActivityForResult(Intent intent, int requestCode);

    void onClearItemDetails();

    void onClearPlaylist();

    void onFillItemDetails(DataItem item);

    void onHttpRequestResult(List<Song> list);

    void onSaved();

    void onSaveItem(String name);

    void sort();

    void search(String query);

    void search(boolean focused);

    UserFragment userFragment();

    UserMap areaFragment();

    PlayWeb getWebView();

}
