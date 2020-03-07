package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongWrite;

import java.util.List;

import static com.tregz.miksing.home.list.song.main.SongMainFragment.EVERYTHING;

class SongInsert extends DataSingle<List<Long>> {
    //private String TAG = SongInsert.class.getSimpleName();

    private TubeSong join;

    SongInsert(Context context, final Song song, final TubeSong join) {
        super(context);
        this.join = join;
        subscribe(DataReference.getInstance(context).accessSong().insert(song));
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        new TubeSongWrite(context, join);
        new TubeSongWrite(context, new TubeSong(EVERYTHING, join.getSongId()));
    }
}
