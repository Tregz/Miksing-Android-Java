package com.tregz.miksing.data.tube;

import android.content.Context;

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
import com.tregz.miksing.data.user.tube.UserTubeWrite;

import java.util.Date;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TubeListener implements MaybeObserver<Tube>, TubeInsert.OnSave,
        ValueEventListener {
    //private String TAG = TubeListener.class.getSimpleName();

    private Context context;
    private UserTube join;
    private DatabaseReference table;

    private Tube tube;
    private TubeAccess access;

    public TubeListener(Context context, UserTube join) {
        this.context = context;
        this.join = join;
        table().child(join.getTubeId()).child("data").addListenerForSingleValueEvent(this);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // do nothing
    }

    @Override
    public void onComplete() {
        new TubeWrite(context, tube, this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String name = snapshot.child("name").getValue(String.class);
        Long createdAt = snapshot.child("copy").getValue(Long.class);
        if (name != null && createdAt != null) {
            tube = new Tube(join.getTubeId(), new Date(createdAt), name);
            subscribe(access().whereId(join.getTubeId()));
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
        if (join != null) {
            new UserTubeWrite(context, join);
            new TubeSongListener(context, join.getTubeId());
        }
    }

    private void subscribe(Maybe<Tube> maybe) {
        maybe.subscribeOn(Schedulers.io()).subscribe(this);
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
