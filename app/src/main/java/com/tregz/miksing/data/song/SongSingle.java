package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

abstract class SongSingle<T> extends DataSingle<T> {

    SongSingle(Context context) {
        super(context);
    }

    SongAccess access() {
        return DataReference.getInstance(context).accessSong();
    }
}
