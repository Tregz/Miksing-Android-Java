package com.tregz.miksing.data.tube.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.data.DataSingle;

import java.util.List;

import io.reactivex.Single;

public class TubeSongInsert extends DataSingle<List<Long>> {

    private String tubeId;

    public TubeSongInsert(Context context, Single<List<Long>> single) {
        this.context = context;
        subscribe(single);
    }

    public TubeSongInsert(Context context, Single<List<Long>> single, String tubeId) {
        this.context = context;
        this.tubeId = tubeId;
        subscribe(single);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (BuildConfig.DEBUG) Log.d(TAG, "TubeSong tubeId: " + tubeId);
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
    }
}
