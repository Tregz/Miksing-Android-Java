package com.tregz.miksing.data.item.work.song;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.base.BaseRealtime;
import com.tregz.miksing.data.DataNotation;

import java.util.Date;

public class SongRealtime extends BaseRealtime {
    private String TAG = SongRealtime.class.getSimpleName();

    private Context context;

    public SongRealtime(Context context) {
        this.context = context;
        FirebaseDatabase.getInstance().getReference("song").addChildEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (snapshot.getKey() != null) {
            Song song = new Song(snapshot.getKey(), new Date(getLong(snapshot, DataNotation.CD)));
            song.setReleasedAt(new Date(getLong(snapshot, DataNotation.BD)));
            song.setUpdatedAt(new Date(getLong(snapshot, DataNotation.ED)));
            song.setFeaturing(getString(snapshot, DataNotation.FS));
            song.setMixedBy(getString(snapshot, DataNotation.LS));
            song.setArtist(getString(snapshot, DataNotation.MS));
            song.setTitle(getString(snapshot, DataNotation.NS));
            song.setWhat(getInt(snapshot, DataNotation.WI));
            Log.d(TAG, "Song added: " + song.getArtist() + " - " + song.getTitle());
            new SongInsert(context, song);
        }
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
