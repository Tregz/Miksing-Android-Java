package com.tregz.miksing.data.tube.song;

import android.content.Context;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TubeSongSaver implements MaybeObserver<TubeSong> {

    private TubeSongAccess access;
    private TubeSong join;
    private Context context;

    public TubeSongSaver(Context context, TubeSong join) {
        this.context = context;
        this.join = join;
        access().whereId(join.getId()).subscribeOn(Schedulers.io()).subscribe(this);
    }

    @Override
    public void onComplete() {
        new TubeSongInsert(access().insert(join), join);
    }

    @Override
    public void onError(Throwable e) {
        //
    }

    @Override
    public void onSubscribe(Disposable d) {
        //
    }

    @Override
    public void onSuccess(TubeSong tubeSong) {
        new DataUpdate(access().update(join));
    }

    private TubeSongAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTubeSong();
        return access;
    }
}
