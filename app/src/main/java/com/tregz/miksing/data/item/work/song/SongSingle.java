package com.tregz.miksing.data.item.work.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataAccess;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class SongSingle<T> implements SingleObserver<T> {
    protected String TAG = SongSingle.class.getSimpleName();

    private Context context;

    SongSingle(Context context) {
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

    SongAccess access() {
        return DataAccess.getInstance(context).songAccess();
    }
}
