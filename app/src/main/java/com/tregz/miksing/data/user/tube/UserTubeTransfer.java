package com.tregz.miksing.data.user.tube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.data.tube.song.TubeSongRelation;

import java.util.Date;
import java.util.List;

public class UserTubeTransfer {

    private DatabaseReference tubeRef = FirebaseDatabase.getInstance().getReference("tube");
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");
    private String userId;
    private String tubeId;
    private int position;

    public UserTubeTransfer(@NonNull String userId, @Nullable String tubeId, int position) {
        this.userId = userId;
        this.tubeId = tubeId;
        this.position = position;
    }

    public void upload(
            @NonNull String name,
            @Nullable List<TubeSongRelation> relations,
            @Nullable OnNewUserTube listener,
            boolean paste
    ) {
        // Insert or update
        boolean create = tubeId == null || paste;
        DatabaseReference node = create ? tubeRef.push() : tubeRef.child(tubeId);
        if (create && listener != null && node.getKey() != null)
            listener.onUserTubePushed(tubeId, node.getKey());
        // Playlist's info
        node.child("data").child("name").setValue(name);
        node.child("data").child("copy").setValue(new Date().getTime());
        // Playlist's song
        if (relations != null) for (TubeSongRelation relation : relations) {
            int position = relation.join.getPosition();
            node.child("song").child(relation.join.getSongId()).setValue(position);
        }
        // Playlist's user
        node.child("user").child(userId).setValue(true);
        // User's playlist
        if (node.getKey() != null)
            userRef.child(userId).child("tube").child(node.getKey()).setValue(position);
    }

    public interface OnNewUserTube {
        void onUserTubePushed(@Nullable String oldTubeId, @NonNull String newTubeId);
    }
}
