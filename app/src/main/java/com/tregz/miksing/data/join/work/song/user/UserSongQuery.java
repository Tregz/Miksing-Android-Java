package com.tregz.miksing.data.join.work.song.user;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.tregz.miksing.data.DataReference;

public class UserSongQuery /* extends UserSongObservable<UserSong>*/ {

    /* public UserSongQuery(Context context) {
        super(context);
    } */

    public LiveData<UserSong> query(Context context, String id) {
        String request = "SELECT * FROM " + UserSong.TABLE + " WHERE id = '" + id + "'";
        UserSongAccess access = DataReference.getInstance(context).accessUserSong();
        return access.item(new SimpleSQLiteQuery(request));
    }

    /*
    public Observable<List<UserSong>> query(String id) {
        String request = "SELECT * FROM " + UserSong.TABLE + " WHERE id = '" + id + "'";
        return access().get(new SimpleSQLiteQuery(request));
    } */
}
