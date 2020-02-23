package com.tregz.miksing.data.user;

import android.util.Log;

import com.tregz.miksing.data.DataSingle;

import java.util.List;

public class UserInsert extends DataSingle<List<Long>> {

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        Log.d(TAG, "user inserted");
    }
}
