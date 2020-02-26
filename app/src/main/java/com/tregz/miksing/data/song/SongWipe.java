package com.tregz.miksing.data.song;

import android.content.Context;

public class SongWipe extends SongSingle<Integer> {

    public SongWipe(Context context) {
        super(context);
        subscribe(access().wipe());
    }
}
