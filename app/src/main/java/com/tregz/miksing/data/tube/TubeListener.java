package com.tregz.miksing.data.tube;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.song.SongListener;
import com.tregz.miksing.data.user.tube.UserTube;

import java.util.Date;

public class TubeListener extends DataListener implements ValueEventListener {
    private String TAG = TubeListener.class.getSimpleName();

    private Context context;
    private String userId;
    private String tubeId;
    private String name;
    private DatabaseReference table;

    public TubeListener(Context context, String userId, String tubeId) {
        this.context = context;
        this.userId = userId;
        this.tubeId = tubeId;
        table().child(tubeId).child("name").addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String name = snapshot.getValue(String.class);
        // TODO set and get createdAt
        if (name != null) {

            Tube tube = new Tube(tubeId, new Date(), name);
            UserTube join = new UserTube(userId, tubeId);
            new TubeInsert(context, tube, join); // TODO listener when inserted
            table().child(tubeId).child(Song.TABLE).addChildEventListener(this);
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {


        //new TubeSongListener(context, snapshot.getRef());
        //
        Log.d(TAG, "Tube's song: " + snapshot.getKey());
        new SongListener(context, snapshot.getKey());
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

    private DatabaseReference table() {
        if (table == null) table = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
        return table;
    }
}
