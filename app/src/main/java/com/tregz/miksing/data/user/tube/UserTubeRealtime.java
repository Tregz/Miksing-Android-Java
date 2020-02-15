package com.tregz.miksing.data.user.tube;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.base.BaseRealtime;
import com.tregz.miksing.data.tube.song.TubeSong;

public class UserTubeRealtime extends BaseRealtime {

    private Context context;
    private String userId;

    public UserTubeRealtime(Context context, String userId) {
        this.context = context;
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("user");
        users.child(userId).child("song").child("mine").addChildEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (snapshot.getKey() != null) {
            UserTube userTube = new UserTube(userId, snapshot.getKey());
            new UserTubeInsert(context, userTube);
            for (DataSnapshot children : snapshot.getChildren()) if (children.getKey() != null) {
                TubeSong tubeSong = new TubeSong(snapshot.getKey(), children.getKey());
                // TODO if TubeSongQuery.
            }
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
