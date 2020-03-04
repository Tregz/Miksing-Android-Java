package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

public class SongDelete extends DataSingle<Integer> {

    public SongDelete(Context context) {
        super(context);
    }

    public void clear() {
        subscribe(DataReference.getInstance(context).accessSong().clear());
    }

    public void wipe() {
        subscribe(DataReference.getInstance(context).accessSong().wipe());
    }
}
