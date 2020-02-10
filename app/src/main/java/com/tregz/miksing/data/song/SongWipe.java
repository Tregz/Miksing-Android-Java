package com.tregz.miksing.data.song;

import android.content.Context;

import io.reactivex.schedulers.Schedulers;

public class SongWipe extends SongSingle<Integer> {

    public SongWipe(Context context) {
        super(context);
        access().wipe().subscribeOn(Schedulers.io()).subscribe(this);
    }
}
