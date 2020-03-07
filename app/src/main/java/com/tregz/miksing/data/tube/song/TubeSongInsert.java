package com.tregz.miksing.data.tube.song;

import android.content.Context;
import com.tregz.miksing.data.DataSingle;

import java.util.List;

import io.reactivex.Single;

public class TubeSongInsert extends DataSingle<List<Long>> {

    public TubeSongInsert(Context context, Single<List<Long>> single) {
        this.context = context;
        subscribe(single);
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
    }
}
