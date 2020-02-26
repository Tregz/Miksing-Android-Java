package com.tregz.miksing.data.tube.song;

import android.content.Context;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TubeSongDelete extends TubeSongSingle<Integer> {

    public TubeSongDelete(Context context, final TubeSong...joins) {
        super(context);
        access().delete(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }

    public TubeSongDelete(Context context, final String id) {
        super(context);
        access().delete(id).subscribeOn(Schedulers.io()).subscribe(this);
    }

}
