package com.tregz.miksing.data.tube;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.tube.song.TubeSongListener;
import com.tregz.miksing.data.user.tube.UserTube;
import com.tregz.miksing.data.user.tube.UserTubeSaver;

import java.util.Date;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TubeListener implements MaybeObserver<Tube>, TubeInsert.OnSave,
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
        // do nothing
    }

    @Override
    public void onComplete() {
        new TubeInsert(tube, this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String name = snapshot.getValue(String.class);
        // TODO set and get createdAt
        if (name != null) {
            tube = new Tube(tubeId, new Date(), name);
            access().whereId(tubeId).subscribeOn(Schedulers.io()).subscribe(this);
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
        new DataUpdate(access().update(this.tube));
        saved();
    }

    @Override
    public void saved() {
        Log.d(TAG, "Saving UserTube user " + userId + " tube: " + tubeId);
        new UserTubeSaver(context, new UserTube(userId, tubeId));
        new TubeSongListener(context, tubeId);
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
