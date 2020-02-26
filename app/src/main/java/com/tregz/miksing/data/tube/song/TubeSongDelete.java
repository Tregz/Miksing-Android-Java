package com.tregz.miksing.data.tube.song;

import android.content.Context;

public class TubeSongDelete extends TubeSongSingle<Integer> {

    public TubeSongDelete(Context context, final TubeSong...joins) {
        super(context);
        subscribe(access().delete(joins));
    }

    public TubeSongDelete(Context context, final String songId, final String tubeId) {
        super(context);
        subscribe(access().delete(songId, tubeId));
    }
}
