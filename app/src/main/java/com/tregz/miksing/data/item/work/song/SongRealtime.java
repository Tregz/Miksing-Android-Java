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
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (snapshot.getKey() != null) {
            Song song = new Song(snapshot.getKey(), new Date());
            song.setArtist(getString(snapshot, Song.MARK_BRAND));
            song.setDirty(!getBoolean(snapshot, Song.RADIO_EDIT));
            song.setMix(getInt(snapshot, Song.MIX_RECORD));
            song.setMixedBy(getString(snapshot, Song.PROD_MAKER));
            song.setReleasedAt(new Date(getLong(snapshot, Song.BORN_SINCE)));
            song.setTitle(getString(snapshot, Song.NAME_GIVEN));
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
