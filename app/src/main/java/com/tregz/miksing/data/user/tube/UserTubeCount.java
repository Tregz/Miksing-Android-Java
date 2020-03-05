package com.tregz.miksing.data.user.tube;

import android.content.Context;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;

import java.util.List;

public class UserTubeCount extends DataMaybe<List<UserTubeRelation>> {

    private Total listener;

    public UserTubeCount(Context context, Total listener, String userId) {
        this.listener = listener;
        subscribe(DataReference.getInstance(context).accessUserTube().whereUser(userId));
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
