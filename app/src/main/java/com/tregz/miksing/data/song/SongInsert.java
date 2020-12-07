package com.tregz.miksing.data.song;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongWrite;

import java.util.List;

class SongInsert extends DataSingle<List<Long>> {
    private final String TAG = SongInsert.class.getSimpleName();

    private final TubeSong join;

    SongInsert(Context context, final Song song, final TubeSong join) {
        super(context);
        this.join = join;
        if (BuildConfig.DEBUG) Log.d(TAG, "Should insert song");
        subscribe(DataReference.getInstance(context).accessSong().insert(song));
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        if (BuildConfig.DEBUG) Log.d(TAG, "SongInsert success");
        /* new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 10); */
        new TubeSongWrite(context, join);
    }
}
