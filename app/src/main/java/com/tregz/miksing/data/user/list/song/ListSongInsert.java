package com.tregz.miksing.data.user.list.song;

import android.content.Context;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class ListSongInsert extends ListSongSingle<List<Long>> {

    public ListSongInsert(Context context, final ListSong...joins) {
        super(context);
        access().insert(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
