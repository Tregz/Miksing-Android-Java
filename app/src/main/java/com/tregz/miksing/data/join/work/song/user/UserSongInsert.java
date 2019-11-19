package com.tregz.miksing.data.join.work.song.user;

import android.content.Context;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class UserSongInsert extends UserSongSingle<List<Long>> {

    public UserSongInsert(Context context, final UserSong...joins) {
        super(context);
        access().insert(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
