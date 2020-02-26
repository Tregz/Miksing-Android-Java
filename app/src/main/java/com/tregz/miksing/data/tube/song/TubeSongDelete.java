package com.tregz.miksing.data.tube.song;

import android.content.Context;

import io.reactivex.schedulers.Schedulers;

public class TubeSongDelete extends TubeSongSingle<Integer> {

    public TubeSongDelete(Context context, final TubeSong...joins) {
        super(context);
        access().delete(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }

}
