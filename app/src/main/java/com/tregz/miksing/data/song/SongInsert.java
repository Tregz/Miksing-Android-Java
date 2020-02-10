package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.user.list.song.ListSong;
import com.tregz.miksing.data.user.list.song.ListSongInsert;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

class SongInsert extends SongSingle<List<Long>> {

    SongInsert(Context context, final String listId, final Song...songs) {
        super(context);
        access().insert(songs).subscribeOn(Schedulers.io()).subscribe(this);
        for (Song song : songs) {
            ListSong join = new ListSong(listId, song.getId());
            new ListSongInsert(context, join);
        }
    }
}
