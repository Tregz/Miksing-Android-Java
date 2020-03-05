package com.tregz.miksing.data.user.tube;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;

import java.util.List;

public class UserTubeQuery extends DataMaybe<List<UserTubeRelation>> {

    private OnUserTubeQueryDataResultCallback listener;
    private UserTubeAccess access;

    public UserTubeQuery(
            @NonNull Context context,
            @NonNull OnUserTubeQueryDataResultCallback listener
    ) {
        this.listener = listener;
        access = DataReference.getInstance(context).accessUserTube();
    }

    public void whereUser(@NonNull String userId) {
        subscribe(access.whereUser(userId));
    }

    @Override
    public void onComplete() {
        // do something ?
    }

    @Override
    public void onSuccess(List<UserTubeRelation> relations) {
        listener.onUserTubeQueryDataResult(relations);
    }

    public interface OnUserTubeQueryDataResultCallback {
        void onUserTubeQueryDataResult(List<UserTubeRelation> relations);
    }
}
