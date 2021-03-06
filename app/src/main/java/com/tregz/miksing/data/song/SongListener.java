package com.tregz.miksing.data.song;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.TubeCreate;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongInsert;
import com.tregz.miksing.data.user.User;

import java.util.Date;

public class SongListener extends DataListener implements ValueEventListener, TubeCreate.OnUserSongListener {
    private String TAG = SongListener.class.getSimpleName();

    private final Context context;
    private TubeSong join;
    private String userId;

    /** Playlist's song */
    public SongListener(Context context, String key, TubeSong join) {
        this.context = context;
        this.join = join;
        DatabaseReference songRef = FirebaseDatabase.getInstance().getReference(Song.TABLE);
        songRef.child(key).addValueEventListener(this);
    }

    /** User's song */
    public SongListener(Context context, String userId) {
        this.context = context;
        this.userId = userId;

        Tube main = new Tube(userId, new Date(), null);
        new TubeCreate(context,  userId, null, main, this); // main list
    }

    @Override
    public void onUserSongListener() {
        Log.d(TAG, "onUserSongListener");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(User.TABLE);
        userRef.child(userId).child(Song.TABLE).addChildEventListener(this);
        //new SongCount(context, userRef.child(userId).child(Song.TABLE), userId);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Song song = songFrom(snapshot);
        TubeSong join = this.join != null ? this.join : new TubeSong(userId, song.getId());
        if (song != null) new SongWrite(context, song, join);
    }

    private Song songFrom(@NonNull DataSnapshot snapshot) {
        if (snapshot.getKey() != null) {
            Song song = new Song(snapshot.getKey(), new Date(getLong(snapshot, DataNotation.CD)));
            song.setReleasedAt(new Date(getLong(snapshot, DataNotation.RD)));
            song.setUpdatedAt(new Date(getLong(snapshot, DataNotation.UD)));
            song.setFeaturing(getString(snapshot, DataNotation.FS));
            song.setMixedBy(getString(snapshot, DataNotation.MS));
            song.setArtist(getString(snapshot, DataNotation.AS));
            song.setTitle(getString(snapshot, DataNotation.NS));
            song.setVersion(getInt(snapshot, DataNotation.VI));
            return song;
        } else return null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        if (snapshot.getKey() != null && userId != null) {
            if (BuildConfig.DEBUG) Log.d(TAG, "onChildAdded");
            join = new TubeSong(userId, snapshot.getKey());
            new TubeSongInsert(context, DataReference.getInstance(context).accessTubeSong().insert(join));
            //DatabaseReference songRef = FirebaseDatabase.getInstance().getReference(Song.TABLE);
            //songRef.child(snapshot.getKey()).addValueEventListener(this);
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onChildAdded");
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
