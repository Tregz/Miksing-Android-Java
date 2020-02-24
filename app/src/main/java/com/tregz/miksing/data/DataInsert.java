package com.tregz.miksing.data;

import android.util.Log;

import java.util.List;

public class DataInsert extends DataSingle<List<Long>> {

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        Log.d(TAG, "Data inserted");
    }
}
