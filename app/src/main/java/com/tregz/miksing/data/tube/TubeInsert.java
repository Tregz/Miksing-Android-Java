package com.tregz.miksing.data.tube;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

import java.util.List;

import io.reactivex.Single;

public class TubeInsert extends DataSingle<List<Long>> {
    protected String TAG = TubeInsert.class.getSimpleName();

    private TubeInsert.OnSave listener;

    TubeInsert(Tube tube, TubeInsert.OnSave listener) {
        this.listener = listener;
        TubeAccess access = DataReference.getInstance(context).accessTube();
        subscribe(access.insert(tube));
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        Log.d(TAG, "Saved " + longs.toString());
        listener.saved();
    }

    interface OnSave {
        void saved();
    }
}
