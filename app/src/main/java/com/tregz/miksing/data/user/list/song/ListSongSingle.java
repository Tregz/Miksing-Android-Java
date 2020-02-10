package com.tregz.miksing.data.user.list.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataReference;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class ListSongSingle<T> implements SingleObserver<T> {
    protected String TAG = ListSongSingle.class.getSimpleName();

    private Context context;

    ListSongSingle(Context context) {
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

    ListSongAccess access() {
        return DataReference.getInstance(context).accessUserSong();
    }
}
