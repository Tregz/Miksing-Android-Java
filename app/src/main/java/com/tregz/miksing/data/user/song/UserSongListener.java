package com.tregz.miksing.data.user.song;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.data.DataInsert;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongAccess;
import com.tregz.miksing.data.tube.song.TubeSongInsert;
import com.tregz.miksing.data.user.User;

import static com.tregz.miksing.data.Data.UNDEFINED;

public class UserSongListener extends DataListener {
    private final String TAG = UserSongListener.class.getSimpleName();

    private final Context context;
    private final String userId;

    public UserSongListener(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        DatabaseReference users = FirebaseDatabase.getInstance().getReference(User.TABLE);
        users.child(userId).child(Song.TABLE).addChildEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        // do nothing, songs are added on firebase song events
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onChildChanged id: " + snapshot.getKey());
        new DataUpdate(DataReference.getInstance(context).accessTubeSong().update(join(snapshot)));
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
        // TODO
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        // TODO
    }

    private TubeSong join(@NonNull DataSnapshot snapshot) {
        if (snapshot.getKey() != null) {
            TubeSong join = new TubeSong(userId, snapshot.getKey());
            Integer position = snapshot.getValue(Integer.class);
            if (position != null) join.setPosition(position);
            return join;
        } else return null;
    }
}
