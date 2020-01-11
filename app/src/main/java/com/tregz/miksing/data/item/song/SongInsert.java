package com.tregz.miksing.data.item.song;

import android.content.Context;

import com.tregz.miksing.data.join.song.user.UserSong;
import com.tregz.miksing.data.join.song.user.UserSongInsert;

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
