package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongInsert;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

class SongInsert extends SongSingle<List<Long>> {

    SongInsert(Context context, final String listId, final Song...songs) {
        super(context);
        access().insert(songs).subscribeOn(Schedulers.io()).subscribe(this);
        for (Song song : songs) {
            TubeSong join = new TubeSong(listId, song.getId());
            new TubeSongInsert(context, join);
        }
    }
}
