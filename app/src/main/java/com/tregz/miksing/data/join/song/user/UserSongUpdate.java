package com.tregz.miksing.data.join.song.user;

import android.content.Context;

import io.reactivex.schedulers.Schedulers;

public class UserSongUpdate extends UserSongSingle<Integer> {

    public UserSongUpdate(Context context, final UserSong...joins) {
        super(context);
        access().update(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
