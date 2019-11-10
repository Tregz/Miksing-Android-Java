package com.tregz.miksing.data.item.work.song;

import android.content.Context;

import com.tregz.miksing.data.DataAccess;
import com.tregz.miksing.data.item.work.song.SongAccess;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class SongSingle<T> implements SingleObserver<T> {
    //private String TAG = SongSingle.class.getSimpleName();

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
        // do nothing
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    SongAccess access() {
        return DataAccess.getInstance(context).songAccess();
    }
}
