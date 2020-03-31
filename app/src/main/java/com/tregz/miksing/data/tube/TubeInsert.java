package com.tregz.miksing.data.tube;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

import java.util.List;

public class TubeInsert extends DataSingle<List<Long>> {
    protected String TAG = TubeInsert.class.getSimpleName();

    private TubeInsert.OnSave listener;

    TubeInsert(
            @NonNull Context context,
            @NonNull Tube tube,
            @Nullable TubeInsert.OnSave listener
    ) {
        this.listener = listener;
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
