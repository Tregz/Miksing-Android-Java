package com.tregz.miksing.data.item.work.song;

import android.content.Context;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class SongInsert extends SongSingle<List<Long>> {

    public SongInsert(Context context, final Song...songs) {
        super(context);
        access().insert(songs).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
