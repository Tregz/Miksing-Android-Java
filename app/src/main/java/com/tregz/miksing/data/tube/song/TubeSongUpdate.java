package com.tregz.miksing.data.tube.song;

import android.content.Context;

import io.reactivex.schedulers.Schedulers;

public class TubeSongUpdate extends TubeSongSingle<Integer> {

    public TubeSongUpdate(Context context, final TubeSong...joins) {
        super(context);
        access().update(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
