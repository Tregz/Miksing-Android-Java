package com.tregz.miksing.data.join.work.song.user;

import android.content.Context;

import com.tregz.miksing.data.DataReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class UserSongObservable<T> implements Observer<T> {
    protected String TAG = UserSongObservable.class.getSimpleName();

    private Context context;

    UserSongObservable(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        // do nothing
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        // do nothing
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    UserSongAccess access() {
        return DataReference.getInstance(context).accessUserSong();
    }
}
