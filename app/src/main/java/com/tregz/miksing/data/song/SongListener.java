package com.tregz.miksing.data.song;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.tube.song.TubeSong;

import java.util.Date;

public class SongListener extends DataListener implements ValueEventListener {
    private String TAG = SongListener.class.getSimpleName();

    private Context context;
    private TubeSong join;

    public SongListener(Context context, String key, TubeSong join) {
        this.context = context;
        this.join = join;
        //FirebaseDatabase.getInstance().getReference(Song.TABLE).addChildEventListener(this);
        DatabaseReference song = FirebaseDatabase.getInstance().getReference(Song.TABLE);
        song.child(key).addValueEventListener(this); //addChildEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.getKey() != null) {
            Song song = new Song(snapshot.getKey(), new Date(getLong(snapshot, DataNotation.CD)));
            song.setReleasedAt(new Date(getLong(snapshot, DataNotation.BD)));
            song.setUpdatedAt(new Date(getLong(snapshot, DataNotation.ED)));
            song.setFeaturing(getString(snapshot, DataNotation.FS));
            song.setMixedBy(getString(snapshot, DataNotation.LS));
            song.setArtist(getString(snapshot, DataNotation.MS));
            song.setTitle(getString(snapshot, DataNotation.NS));
            song.setWhat(getInt(snapshot, DataNotation.WI));
            new SongWrite(context, song, join);
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        /* if (snapshot.getKey() != null) {
            Song song = new Song(snapshot.getKey(), new Date(getLong(snapshot, DataNotation.CD)));
            song.setReleasedAt(new Date(getLong(snapshot, DataNotation.BD)));
            song.setUpdatedAt(new Date(getLong(snapshot, DataNotation.ED)));
            song.setFeaturing(getString(snapshot, DataNotation.FS));
            song.setMixedBy(getString(snapshot, DataNotation.LS));
            song.setArtist(getString(snapshot, DataNotation.MS));
            song.setTitle(getString(snapshot, DataNotation.NS));
            song.setWhat(getInt(snapshot, DataNotation.WI));
            //Log.d(TAG, "Song key: " + song.getArtist() + " - " + song.getTitle());
            //Log.d(TAG, "Song added: " + song.getArtist() + " - " + song.getTitle());
            new SongInsert(context, song);
        } */
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        // TODO
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        // TODO
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        // TODO
    }
}
