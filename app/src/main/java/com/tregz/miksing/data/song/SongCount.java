package com.tregz.miksing.data.song;

import android.content.Context;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;

import java.util.List;

public class SongCount extends DataMaybe<List<Song>> {

    private Total listener;

    public SongCount(Context context, Total listener) {
        this.listener = listener;
        subscribe(DataReference.getInstance(context).accessSong().songList());
    }

    @Override
    public void onComplete() {
        // do nothing
    }

    @Override
    public void onSuccess(List<Song> songs) {
        listener.size(songs.size());
    }

    public interface Total {
        void size(int number);
    }
}
