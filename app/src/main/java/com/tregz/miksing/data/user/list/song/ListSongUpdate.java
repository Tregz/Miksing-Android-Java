package com.tregz.miksing.data.user.list.song;

import android.content.Context;

import io.reactivex.schedulers.Schedulers;

public class ListSongUpdate extends ListSongSingle<Integer> {

    public ListSongUpdate(Context context, final ListSong...joins) {
        super(context);
        access().update(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
