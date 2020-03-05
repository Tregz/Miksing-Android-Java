package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongWrite;

class SongWrite extends DataMaybe<Song> {

    private SongAccess access;
    private Context context;
    private TubeSong join;
    private Song song;

    SongWrite(Context context, Song song, TubeSong join) {
        this.context = context;
        this.join = join;
        this.song = song;
        subscribe(access().whereId(song.getId()));
    }

    @Override
    public void onComplete() {
        new SongInsert(context, song, join);
    }

    @Override
    public void onSuccess(Song tubeSong) {
        new DataUpdate(access().update(song));
        new TubeSongWrite(context, join);
    }

    private SongAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessSong();
        return access;
    }

}
