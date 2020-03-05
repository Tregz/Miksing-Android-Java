package com.tregz.miksing.data.user.tube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.data.tube.song.TubeSongRelation;

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
        // Playlist's song
        boolean create = tubeId == null || paste;
        DatabaseReference node = create ? tubeRef.push() : tubeRef.child(tubeId);
        if (create && listener != null && node.getKey() != null)
            listener.onUserTubePushed(tubeId, node.getKey());
        node.child("name").setValue(name);
        if (relations != null) for (TubeSongRelation relation : relations) {
            int position = relation.join.getPosition();
            node.child("song").child(relation.join.getSongId()).setValue(position);
        }
        // User's playlist
        if (node.getKey() != null)
            userRef.child(userId).child("tube").child(node.getKey()).setValue(position);
    }

    public interface OnNewUserTube {
        void onUserTubePushed(@Nullable String oldTubeId, @NonNull String newTubeId);
    }
}
