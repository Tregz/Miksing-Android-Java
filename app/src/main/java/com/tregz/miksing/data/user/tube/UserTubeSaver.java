package com.tregz.miksing.data.user.tube;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;

public class UserTubeSaver extends DataMaybe<UserTube> {
    protected String TAG = UserTubeSaver.class.getSimpleName();

    private UserTubeAccess access;
    private UserTube join;
    private Context context;

    public UserTubeSaver(Context context, UserTube join) {
        this.context = context;
        this.join = join;
        subscribe(access().whereId(join.getId()));
    }

    @Override
    public void onComplete() {
        new UserTubeInsert(access().insert(join));
        Log.d(TAG, "onComplete join:" + join.getUserId());
    }

    @Override
    public void onSuccess(UserTube join) {
        new DataUpdate(access().update(this.join));
        Log.d(TAG, "onSuccess join:" + join.getUserId());
    }

    private UserTubeAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessUserTube();
        return access;
    }
}
