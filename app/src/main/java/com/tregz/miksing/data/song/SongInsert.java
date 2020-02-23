package com.tregz.miksing.data.song;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongInsert;

import java.util.Date;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class SongInsert extends SongSingle<List<Long>> {

    public SongInsert(Context context, final Song...songs) {
        super(context);



        access().insert(songs).subscribeOn(Schedulers.io()).subscribe(this);
        for (Song song : songs) {
            //if (ref.getKey() != null) {

                //ref.setValue(SongUtil.map(song)); // song will be inserted on local database onChildAdded
                // TODO listener.onSaved();
            //}
            // song will be inserted on local database onChildAdded
            //TubeSong join = new TubeSong(listId, song.getId());
            //new TubeSongInsert(context, join);
        }
    }
}
