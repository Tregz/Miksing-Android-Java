package com.tregz.miksing.data.tube;

import android.content.Context;

import com.tregz.miksing.data.user.tube.UserTube;
import com.tregz.miksing.data.user.tube.UserTubeInsert;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class TubeInsert extends TubeSingle<List<Long>> {

    private UserTube join;
    private Context context;

    public TubeInsert(Context context, final Tube data, final UserTube join) {
        super(context);
        this.context = context;
        this.join = join;
        access().insert(data).subscribeOn(Schedulers.io()).subscribe(this);
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        new UserTubeInsert(context, join);
    }
}
