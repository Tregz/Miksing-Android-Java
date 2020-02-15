package com.tregz.miksing.data.user.tube;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataReference;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class UserTubeSingle<T> implements SingleObserver<T> {
    protected String TAG = UserTubeSingle.class.getSimpleName();

    private Context context;

    UserTubeSingle(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        // do nothing
    }

    @Override
    public void onSuccess(T t) {
        Log.d(TAG, t.toString());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    UserTubeAccess access() {
        return DataReference.getInstance(context).accessUserList();
    }
}
