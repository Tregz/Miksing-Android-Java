package com.tregz.miksing.data.user;

import android.content.Context;

import com.tregz.miksing.data.user.tube.UserTubeListener;

import java.util.List;

class UserInsert extends UserSingle<List<Long>> {

    private User[] users;

    UserInsert(Context context, final User...users) {
        super(context);
        this.users = users;
        subscribe(access().insert(users));
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        for (User user : users) new UserTubeListener(context, user.getId());
    }
}
