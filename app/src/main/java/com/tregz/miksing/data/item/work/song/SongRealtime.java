package com.tregz.miksing.data.item.work.song;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.base.BaseRealtime;

import java.util.Date;

public class SongRealtime extends BaseRealtime {
    private String TAG = SongRealtime.class.getSimpleName();

    private Context context;

    public SongRealtime(Context context) {
        this.context = context;
        FirebaseDatabase.getInstance().getReference("test").addChildEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (dataSnapshot.getKey() != null) {
            Song song = new Song(dataSnapshot.getKey(), new Date());
            song.setArtist(getString(dataSnapshot, Song.ARTIST));
            song.setKind(getInt(dataSnapshot, Song.KIND));
            song.setMixedBy(getString(dataSnapshot, Song.MIXED_BY));
            song.setReleasedAt(new Date(getLong(dataSnapshot, Song.RELEASED_AT)));
            song.setTitle(getString(dataSnapshot, Song.TITLE));
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
