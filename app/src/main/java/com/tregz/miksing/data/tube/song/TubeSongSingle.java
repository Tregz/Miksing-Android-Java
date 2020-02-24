package com.tregz.miksing.data.tube.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataReference;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class TubeSongSingle<T> implements SingleObserver<T> {
    protected String TAG = TubeSongSingle.class.getSimpleName();

    private Context context;

    TubeSongSingle(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onSubscribe(Disposable d) {
        // do nothing
    }

    @Override
    public void onSuccess(T t) {
        Log.d(TAG, t.toString());
    }

    TubeSongAccess access() {
        return DataReference.getInstance(context).accessTubeSong();
    }
}
