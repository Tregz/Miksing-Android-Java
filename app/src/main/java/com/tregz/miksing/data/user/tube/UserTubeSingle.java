package com.tregz.miksing.data.user.tube;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

public abstract class UserTubeSingle<T> extends DataSingle<T> {
    protected String TAG = UserTubeSingle.class.getSimpleName();

    UserTubeSingle(Context context) {
        super(context);
    }

    UserTubeAccess access() {
        return DataReference.getInstance(context).accessUserTube();
    }
}
