package com.tregz.miksing.data.user.list;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.base.BaseRealtime;
import com.tregz.miksing.data.user.list.song.ListSong;
import com.tregz.miksing.data.user.list.song.ListSongQuery;

public class UserListRealtime extends BaseRealtime {

    private Context context;
    private String userId;

    public UserListRealtime(Context context, String userId) {
        this.context = context;
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("user");
        users.child(userId).child("song").child("mine").addChildEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (snapshot.getKey() != null) {
            UserList userList = new UserList(userId, snapshot.getKey());
            new UserListInsert(context, userList);
            for (DataSnapshot children : snapshot.getChildren()) if (children.getKey() != null) {
                ListSong listSong = new ListSong(snapshot.getKey(), children.getKey());
                // TODO if ListSongQuery.
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
