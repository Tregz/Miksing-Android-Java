package com.tregz.miksing.data.tube.song;

import com.tregz.miksing.data.DataSingle;
import com.tregz.miksing.home.HomeView;

import java.util.List;

import io.reactivex.Single;

class TubeSongInsert extends DataSingle<List<Long>> {

    private TubeSong[] joins;

    TubeSongInsert(Single<List<Long>> single, final TubeSong...joins) {
        this.joins = joins;
        subscribe(single);
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        for (TubeSong join : joins)
        if (context instanceof HomeView) ((HomeView) context).onTubeSongInserted(join.getTubeId());
    }
}
