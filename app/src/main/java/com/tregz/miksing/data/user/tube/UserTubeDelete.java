package com.tregz.miksing.data.user.tube;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.user.User;

public class UserTubeDelete implements UserTubeCount.Total, ValueEventListener {

    private Context context;
    private DatabaseReference tubeRef = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
    private String tubeId;
    private String userId;

    public UserTubeDelete(Context context, String userId, String tubeId) {
        this.context = context;
        this.tubeId = tubeId;
        this.userId = userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(User.TABLE);
        userRef.child(userId).child(Tube.TABLE).child(tubeId).removeValue();
        tubeRef.child(tubeId).child(Song.TABLE).addListenerForSingleValueEvent(this);
        new UserTubeCount(context, this).getUserCount(tubeId);
    }

    @Override
    public void size(int number) {
        if (number < 2) DataReference.getInstance(context).accessTube().delete(tubeId);
        else DataReference.getInstance(context).accessUserTube().delete(userId, tubeId);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // do nothing
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.getChildrenCount() < 2) tubeRef.child(tubeId).removeValue();
    }
}
