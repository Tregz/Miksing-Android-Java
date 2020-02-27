package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongSaver;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class SongSaver implements MaybeObserver<Song> {

    private SongAccess access;
    private Context context;
    private TubeSong join;
    private Song song;

    SongSaver(Context context, Song song, TubeSong join) {
        this.context = context;
        this.join = join;
        this.song = song;
        access().whereId(song.getId()).subscribeOn(Schedulers.io()).subscribe(this);
    }

    @Override
    public void onComplete() {
        new SongInsert(context, song, join);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSubscribe(Disposable d) {
        //
    }

    @Override
    public void onSuccess(Song tubeSong) {
        new DataUpdate(access().update(song));
        new TubeSongSaver(context, join);
    }

    private SongAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessSong();
        return access;
    }

}
