package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongSaver;

import java.util.List;

class SongInsert extends SongSingle<List<Long>> {

    private TubeSong join;

    SongInsert(Context context, final Song song, final TubeSong join) {
        super(context);
        this.join = join;
        subscribe(access().insert(song));
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        new TubeSongSaver(context, join);
    }
}
