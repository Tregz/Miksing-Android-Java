package com.tregz.miksing.data.user.tube;

import android.content.Context;

public class UserTubeUpdate extends UserTubeSingle<Integer> {

    public UserTubeUpdate(Context context, final UserTube...joins) {
        super(context);
        subscribe(access().update(joins));
    }
}
