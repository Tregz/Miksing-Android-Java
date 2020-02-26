package com.tregz.miksing.data.tube.song;

import android.content.Context;

public class TubeSongUpdate extends TubeSongSingle<Integer> {

    public TubeSongUpdate(Context context, final TubeSong...joins) {
        super(context);
        subscribe(access().update(joins));
    }
}
