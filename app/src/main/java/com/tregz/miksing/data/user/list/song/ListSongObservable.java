package com.tregz.miksing.data.user.list.song;

import android.content.Context;

import com.tregz.miksing.data.DataReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ListSongObservable<T> implements Observer<T> {
    protected String TAG = ListSongObservable.class.getSimpleName();

    private Context context;

    ListSongObservable(Context context) {
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

    ListSongAccess access() {
        return DataReference.getInstance(context).accessUserSong();
    }
}
