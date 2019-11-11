package com.tregz.miksing.data.item.work.song;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class SongInsert extends SongSingle<List<Long>> {

    public SongInsert(Context context, final Song...songs) {
        super(context);
        Log.d(TAG, "insert song");
        access().insert(songs).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
