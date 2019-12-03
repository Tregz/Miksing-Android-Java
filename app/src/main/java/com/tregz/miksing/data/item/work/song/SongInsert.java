package com.tregz.miksing.data.item.work.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.join.work.song.user.UserSong;
import com.tregz.miksing.data.join.work.song.user.UserSongInsert;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

class SongInsert extends SongSingle<List<Long>> {

    SongInsert(Context context, final Song...songs) {
        super(context);
        access().insert(songs).subscribeOn(Schedulers.io()).subscribe(this);
        for (Song song : songs) {
            UserSong join = new UserSong(song.getId());
            new UserSongInsert(context, join);
        }
    }
}
