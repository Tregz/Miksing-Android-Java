package com.tregz.miksing.data.join.work.song.user;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataReference;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class UserSongSingle<T> implements SingleObserver<T> {
    protected String TAG = UserSongSingle.class.getSimpleName();

    private Context context;

    UserSongSingle(Context context) {
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

    UserSongAccess access() {
        return DataReference.getInstance(context).accessUserSong();
    }
}
