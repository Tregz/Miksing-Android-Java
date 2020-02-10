package com.tregz.miksing.data.user.list.song;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.tregz.miksing.data.DataReference;

public class ListSongQuery /* extends ListSongObservable<ListSong>*/ {

    /* public ListSongQuery(Context context) {
        super(context);
    } */

    public LiveData<ListSong> query(Context context, String id) {
        String request = "SELECT * FROM " + ListSong.TABLE + " WHERE id = '" + id + "'";
        ListSongAccess access = DataReference.getInstance(context).accessUserSong();
        return access.item(new SimpleSQLiteQuery(request));
    }

    /*
    public Observable<List<ListSong>> query(String id) {
        String request = "SELECT * FROM " + ListSong.TABLE + " WHERE id = '" + id + "'";
        return access().get(new SimpleSQLiteQuery(request));
    } */
}
