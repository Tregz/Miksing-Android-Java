package com.tregz.miksing.data.tube.song;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TubeSongTransfer {
    private String TAG = TubeSongTransfer.class.getSimpleName();

    private DatabaseReference tubeRef = FirebaseDatabase.getInstance().getReference("tube");

    public TubeSongTransfer() {}

    public void upload(TubeSongRelation relation, String newTubeId) {
        String tubeId = newTubeId != null ? newTubeId : relation.join.getTubeId();
        String songId = relation.join.getSongId();
        int position = relation.join.getPosition();
        Log.d(TAG, "tube: " + tubeId + " // song: " + songId);
        tubeRef.child(tubeId).child("song").child(songId).setValue(position);
    }
}
