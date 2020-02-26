package com.tregz.miksing.data.user.tube;

import android.content.Context;

import java.util.List;

public class UserTubeInsert extends UserTubeSingle<List<Long>> {

    public UserTubeInsert(Context context, final UserTube...data) {
        super(context);
        subscribe(access().insert(data));
    }
}
