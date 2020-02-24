package com.tregz.miksing.data;

import android.content.Context;
import android.util.Log;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class DataSingle<T> implements SingleObserver<T> {
    protected String TAG = DataSingle.class.getSimpleName();

    protected Context context;

    public DataSingle() {}

    public DataSingle(Context context) {
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
}
