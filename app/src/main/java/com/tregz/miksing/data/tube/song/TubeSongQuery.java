package com.tregz.miksing.data.tube.song;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.tregz.miksing.data.DataReference;

public class TubeSongQuery /* extends TubeSongObservable<TubeSong>*/ {

    /* public TubeSongQuery(Context context) {
        super(context);
    } */

    public LiveData<TubeSong> query(Context context, String id) {
        String request = "SELECT * FROM " + TubeSong.TABLE + " WHERE id = '" + id + "'";
        TubeSongAccess access = DataReference.getInstance(context).accessTubeSong();
        return access.item(new SimpleSQLiteQuery(request));
    }

    /*
    public Observable<List<TubeSong>> query(String id) {
        String request = "SELECT * FROM " + TubeSong.TABLE + " WHERE id = '" + id + "'";
        return access().get(new SimpleSQLiteQuery(request));
    } */
}
