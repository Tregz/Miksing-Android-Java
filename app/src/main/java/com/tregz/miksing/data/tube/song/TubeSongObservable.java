package com.tregz.miksing.data.tube.song;

import android.content.Context;

import com.tregz.miksing.data.DataReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class TubeSongObservable<T> implements Observer<T> {
    protected String TAG = TubeSongObservable.class.getSimpleName();

    private Context context;

    TubeSongObservable(Context context) {
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

    TubeSongAccess access() {
        return DataReference.getInstance(context).accessListSong();
    }
}
