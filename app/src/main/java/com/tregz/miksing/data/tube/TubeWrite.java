package com.tregz.miksing.data.tube;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;

public class TubeWrite extends DataMaybe<Tube> {
    private final String TAG = TubeWrite.class.getSimpleName();

    private Context context;
    private Tube tube;
    private TubeAccess access;
    private TubeInsert.OnSave listener;

    public TubeWrite(
            @NonNull Context context,
            @NonNull Tube tube,
            @Nullable TubeInsert.OnSave listener
    ) {
        this.context = context;
        this.tube = tube;
        this.listener = listener;
        subscribe(access().whereId(tube.getId()));
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete " + tube.getId());
        new TubeInsert(context, tube, listener);
    }

    @Override
    public void onSuccess(Tube tube) {
        Log.d(TAG, "onSuccess");
        new DataUpdate(access().update(this.tube));
    }

    private TubeAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTube();
        return access;
    }

}
