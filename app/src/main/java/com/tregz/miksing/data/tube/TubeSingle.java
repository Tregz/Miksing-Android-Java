package com.tregz.miksing.data.tube;

import android.util.Log;

import com.tregz.miksing.data.DataSingle;

public class TubeSingle<T> extends DataSingle<T> {
    protected String TAG = TubeSingle.class.getSimpleName();

    private TubeSingle.OnSave listener;

    TubeSingle(TubeSingle.OnSave listener) {
        this.listener = listener;
    }

    @Override
    public void onSuccess(T t) {
        Log.d(TAG, t.toString());
        Log.d(TAG, "tube saved");
        listener.saved();
    }

    interface OnSave {
        void saved();
    }
}
