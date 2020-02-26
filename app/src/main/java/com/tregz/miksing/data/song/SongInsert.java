package com.tregz.miksing.data.song;

import android.content.Context;

import java.util.List;

class SongInsert extends SongSingle<List<Long>> {

    SongInsert(Context context, final Song...songs) {
        super(context);
        subscribe(access().insert(songs));
    }
}
