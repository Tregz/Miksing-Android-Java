package com.tregz.miksing.data.tube.song;

import android.content.Context;

import java.util.List;

public class TubeSongInsert extends TubeSongSingle<List<Long>> {

    public TubeSongInsert(Context context, final TubeSong...joins) {
        super(context);
        subscribe(access().insert(joins));
    }
}
