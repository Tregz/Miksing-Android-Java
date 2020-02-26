package com.tregz.miksing.data.tube.song;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.song.SongListener;

public class TubeSongListener extends DataListener {
    private String TAG = TubeSongListener.class.getSimpleName();

    private Context context;

    public TubeSongListener(Context context, DatabaseReference ref) {
        this.context = context;
        ref.addChildEventListener(this);
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        new SongListener(context, snapshot.getKey());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
        Log.d(TAG, "onChildChanged");
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
        Log.d(TAG, "onChildMoved");
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Log.d(TAG, "onChildRemoved: " + snapshot.getKey());
    }
}
