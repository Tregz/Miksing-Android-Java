package com.tregz.miksing.data;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class DataMaybe<T> implements MaybeObserver<T> {

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSubscribe(Disposable d) {
        //
    }

    protected void subscribe(Maybe<T> single) {
        single.subscribeOn(Schedulers.io()).subscribe(this);
    }

}
