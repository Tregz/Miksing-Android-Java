package com.tregz.miksing.data.item.work.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.join.work.song.user.UserSong;
import com.tregz.miksing.data.join.work.song.user.UserSongInsert;

import java.util.Date;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class SongInsert extends SongSingle<List<Long>> {

    public SongInsert(Context context, final Song...songs) {
        super(context);
        Log.d(TAG, "insert song");
        access().insert(songs).subscribeOn(Schedulers.io()).subscribe(this);

        for (Song song : songs) {
            UserSong join = new UserSong(song.getId());
            Log.d(TAG, "create join position?" + join.getPosition());

            new UserSongInsert(context, join);
        }
    }
}
