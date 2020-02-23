package com.tregz.miksing.data.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.user.tube.UserTubeListener;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserMaybe implements MaybeObserver<User>, ValueEventListener {
    private final String TAG = UserMaybe.class.getSimpleName();

    private boolean exist = false;
    private Context context;
    private DatabaseReference table;
    private User user;
    private UserAccess access;

    public UserMaybe(Context context, User user) {
        this.context = context;
        this.user = user;
        table().child(user.getId()).child("data").addListenerForSingleValueEvent(this);
    }

    private void save() {
        access().query(user.getId()).subscribeOn(Schedulers.io()).subscribe(this);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e(TAG, databaseError.getMessage());
        save();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "exist? " + exist);
        /* if (exist) new UserUpdate(context, user);
        else */ //new UserInsert(context, user);
        access().insert(user).subscribeOn(Schedulers.io()).subscribe(new UserInsert());
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (!dataSnapshot.exists()) {
            table().child(user.getId()).child("data").setValue(UserUtil.map(user));

        } else {
            Log.d(TAG, "User exist");
            table().child(user.getId()).child("data").setValue(UserUtil.map(user));
        }

        save();
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError? " + e.getMessage());
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSuccess(User t) {
        Log.d(TAG, "onSuccess " + t.getName());
        exist = true;
        access().update(user).subscribeOn(Schedulers.io()).subscribe(new DataUpdate());
        new UserTubeListener(context, user.getId());
    }

    private DatabaseReference table() {
        if (table == null) table = FirebaseDatabase.getInstance().getReference(User.TABLE);
        return table;
    }

    private UserAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessUser();
        return access;
    }
}
