package com.tregz.miksing.data.user.list;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.song.SongAccess;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class UserListSingle<T> implements SingleObserver<T> {
    protected String TAG = UserListSingle.class.getSimpleName();

    private Context context;

    UserListSingle(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        // do nothing
    }

    @Override
    public void onSuccess(T t) {
        Log.d(TAG, t.toString());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    UserListAccess access() {
        return DataReference.getInstance(context).accessUserList();
    }
}
