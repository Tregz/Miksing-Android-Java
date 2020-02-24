package com.tregz.miksing.data.user.tube;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.TubeListener;
import com.tregz.miksing.data.user.User;

public class UserTubeListener extends DataListener {
    private String TAG = UserTubeListener.class.getSimpleName();

    private Context context;
    private String userId;

    public UserTubeListener(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        Log.d(TAG, "Get tube's user: " + userId);
        DatabaseReference users = FirebaseDatabase.getInstance().getReference(User.TABLE);
        users.child(userId).child(Tube.TABLE).addChildEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (snapshot.getKey() != null) {

            Log.d(TAG, "    Tube added: " + snapshot.getKey());


            new TubeListener(context, userId, snapshot.getKey());



            /* Tube tube = new Tube(snapshot.getKey(), createdAt, name);
            String name = getString(snapshot, DataNotation.NS);
            Tube tube = new Tube(snapshot.getKey(), createdAt, name);
            Log.d(TAG, "    Tube added: " + tube.getId());
            new TubeInsert(context, "All", tube);

            UserTube userTube = new UserTube(userId, snapshot.getKey());
            new UserTubeInsert(context, userTube);


            DatabaseReference tube = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
            tube.child() */

            /* for (DataSnapshot children : snapshot.getChildren()) if (children.getKey() != null) {
                TubeSong tubeSong = new TubeSong(snapshot.getKey(), children.getKey());
                // TODO if TubeSongQuery.
            } */
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
