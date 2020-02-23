package com.tregz.miksing.data.user.tube;

import android.content.Context;

import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongSingle;

import io.reactivex.schedulers.Schedulers;

public class UserTubeUpdate extends UserTubeSingle<Integer> {

    public UserTubeUpdate(Context context, final UserTube...joins) {
        super(context);
        access().update(joins).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
