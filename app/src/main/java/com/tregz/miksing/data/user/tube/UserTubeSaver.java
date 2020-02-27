package com.tregz.miksing.data.user.tube;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserTubeSaver implements MaybeObserver<UserTube> {
    protected String TAG = UserTubeSaver.class.getSimpleName();

    private UserTubeAccess access;
    private UserTube join;
    private Context context;

    public UserTubeSaver(Context context, UserTube join) {
        this.context = context;
        this.join = join;
        access().whereId(join.getId()).subscribeOn(Schedulers.io()).subscribe(this);
    }

    @Override
    public void onComplete() {
        new UserTubeInsert(access().insert(join));
    }

    @Override
    public void onError(Throwable e) {
        // do nothing
    }

    @Override
    public void onSubscribe(Disposable d) {
        // do nothing
    }

    @Override
    public void onSuccess(UserTube join) {
        new DataUpdate(access().update(this.join));
    }

    private UserTubeAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessUserTube();
        return access;
    }
}
