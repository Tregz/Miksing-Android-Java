package com.tregz.miksing.data.tube.song;

import android.content.Context;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class TubeSongInsert extends TubeSongSingle<List<Long>> {

    public TubeSongInsert(Context context, final TubeSong...joins) {
        super(context);
        access().insert(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
