package com.tregz.miksing.data.user.tube;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.TubeListener;
import com.tregz.miksing.data.user.User;

public class UserTubeListener extends DataListener {
    //private final String TAG = UserTubeListener.class.getSimpleName();

    private Context context;
    private String userId;

    public UserTubeListener(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        //Log.d(TAG, "Get tube's user: " + userId);
        DatabaseReference users = FirebaseDatabase.getInstance().getReference(User.TABLE);
        users.child(userId).child(Tube.TABLE).addChildEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        UserTube join = join(snapshot);
        if (join != null) new TubeListener(context, join);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
        new DataUpdate(DataReference.getInstance(context).accessUserTube().update(join(snapshot)));
    }

    private UserTube join(@NonNull DataSnapshot snapshot) {
        if (snapshot.getKey() != null) {
            UserTube join = new UserTube(userId, snapshot.getKey());
            Integer position = snapshot.getValue(Integer.class);
            if (position != null) join.setPosition(position);
            return join;
        } else return null;
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
        // TODO
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        DataReference.getInstance(context).accessUserTube().delete(userId, snapshot.getKey());
    }
}
