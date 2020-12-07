package com.tregz.miksing.data.tube;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

import java.util.List;

public class TubeInsert extends DataSingle<List<Long>> {
    protected String TAG = TubeInsert.class.getSimpleName();

    private final TubeInsert.OnSave listener;

    TubeInsert(
            @NonNull Context context,
            @NonNull Tube tube,
            @Nullable TubeInsert.OnSave listener
    ) {
        this.listener = listener;
        if (BuildConfig.DEBUG && tube.getId().equals(AuthUtil.userId()))
            Log.d(TAG, "Should insert user's main list");
        subscribe(DataReference.getInstance(context).accessTube().insert(tube));
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        if (listener != null) listener.saved();
    }

    interface OnSave {
        void saved();
    }
}
