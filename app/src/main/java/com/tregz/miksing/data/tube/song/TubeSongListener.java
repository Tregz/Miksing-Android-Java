package com.tregz.miksing.data.tube.song;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.song.SongListener;
import com.tregz.miksing.data.tube.Tube;

public class TubeSongListener extends DataListener {
    private final String TAG = TubeSongListener.class.getSimpleName();

    private final Context context;
    private final String tubeId;
    private TubeSongAccess access;

    public TubeSongListener(Context context, String tubeId) {
        this.context = context;
        this.tubeId = tubeId;
        DatabaseReference table = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
        table.child(tubeId).child(Song.TABLE).addChildEventListener(this);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        super.onCancelled(databaseError);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        /* if (AuthUtil.logged()) {
            TubeSong join = join(snapshot);
            Log.d(TAG, "onChildAdded?");
            if (join != null) new TubeSongWrite(context, join);
        } else new SongListener(context, snapshot.getKey(), join(snapshot)); */
        new SongListener(context, snapshot.getKey(), join(snapshot));
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
        new DataUpdate(access().update(join(snapshot)));
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onChildMoved");
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onChildRemoved: " + snapshot.getKey());
        new DataUpdate(access().delete(snapshot.getKey(), tubeId));
    }

    private TubeSong join(DataSnapshot snapshot) {
        if (snapshot.getKey() != null) {
            TubeSong join = new TubeSong(tubeId, snapshot.getKey());
            Integer position = snapshot.getValue(Integer.class);
            if (position != null) join.setPosition(position);
            return join;
        } else return null;
    }

    private TubeSongAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTubeSong();
        return access;
    }
}
