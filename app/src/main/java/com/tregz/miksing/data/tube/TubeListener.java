package com.tregz.miksing.data.tube;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.song.SongListener;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongInsert;
import com.tregz.miksing.data.user.tube.UserTube;
import com.tregz.miksing.data.user.tube.UserTubeInsert;

import java.util.Date;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TubeListener extends DataListener implements MaybeObserver<Tube>, TubeSingle.OnSave,
        ValueEventListener {
    private String TAG = TubeListener.class.getSimpleName();

    private Context context;
    private String tubeId;
    private String userId;
    private DatabaseReference table;

    private Tube tube;
    private TubeAccess access;

    public TubeListener(Context context, String userId, String tubeId) {
        this.context = context;
        this.userId = userId;
        this.tubeId = tubeId;
        table().child(tubeId).child("name").addListenerForSingleValueEvent(this);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        super.onCancelled(databaseError);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
        Log.d(TAG, "Tube's song: " + snapshot.getKey());
        if (snapshot.getKey() != null) {
            new TubeSongInsert(context, new TubeSong(tubeId, snapshot.getKey()));
            new SongListener(context, snapshot.getKey());
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

    @Override
    public void onComplete() {
        TubeSingle<List<Long>> observer = new TubeSingle<>(this);
        access().insert(tube).subscribeOn(Schedulers.io()).subscribe(observer);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String name = snapshot.getValue(String.class);
        // TODO set and get createdAt
        if (name != null) {
            tube = new Tube(tubeId, new Date(), name);
            access().query(tubeId).subscribeOn(Schedulers.io()).subscribe(this);
        }
    }

    @Override
    public void onError(Throwable e) {
        //
    }

    @Override
    public void onSubscribe(Disposable d) {
        //
    }

    @Override
    public void onSuccess(Tube tube) {
        TubeSingle<Integer> observer = new TubeSingle<>(this);
        access().update(tube).subscribeOn(Schedulers.io()).subscribe(observer);
    }

    @Override
    public void saved() {
        if (userId != null) new UserTubeInsert(context, new UserTube(userId, tubeId));
        DatabaseReference table = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
        table.child(tubeId).child(Song.TABLE).addChildEventListener(this);
    }

    private DatabaseReference table() {
        if (table == null) table = FirebaseDatabase.getInstance().getReference(Tube.TABLE);
        return table;
    }

    private TubeAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTube();
        return access;
    }
}
