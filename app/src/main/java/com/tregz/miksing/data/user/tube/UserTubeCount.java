package com.tregz.miksing.data.user.tube;

import android.content.Context;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;

import java.util.List;

class UserTubeCount extends DataMaybe<List<UserTubeRelation>> {

    private Context context;
    private Total listener;

    UserTubeCount(Context context, Total listener) {
        this.context = context;
        this.listener = listener;
    }

    void getTubeCount(String userId) {
        subscribe(DataReference.getInstance(context).accessUserTube().whereUser(userId));
    }

    void getUserCount(String tubeId) {
        subscribe(DataReference.getInstance(context).accessUserTube().whereTube(tubeId));
    }

    @Override
    public void onComplete() {
        // do nothing
    }

    @Override
    public void onSuccess(List<UserTubeRelation> relations) {
        listener.size(relations.size());
    }

    public interface Total {
        void size(int number);
    }
}
