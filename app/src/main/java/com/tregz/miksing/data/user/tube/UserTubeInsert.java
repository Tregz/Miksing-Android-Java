package com.tregz.miksing.data.user.tube;

import android.content.Context;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

class UserTubeInsert extends UserTubeSingle<List<Long>> {

    UserTubeInsert(Context context, final UserTube...data) {
        super(context);
        access().insert(data).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
