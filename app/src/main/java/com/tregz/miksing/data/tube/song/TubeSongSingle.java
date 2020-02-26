package com.tregz.miksing.data.tube.song;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

abstract class TubeSongSingle<T> extends DataSingle<T> {

    TubeSongSingle(Context context) {
        super(context);
    }

    TubeSongAccess access() {
        return DataReference.getInstance(context).accessTubeSong();
    }
}
