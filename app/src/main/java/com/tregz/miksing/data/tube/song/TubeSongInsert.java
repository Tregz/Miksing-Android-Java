package com.tregz.miksing.data.tube.song;

import android.content.Context;

import com.tregz.miksing.home.HomeView;

import java.util.List;

public class TubeSongInsert extends TubeSongSingle<List<Long>> {

    private TubeSong[] joins;

    public TubeSongInsert(Context context, final TubeSong...joins) {
        super(context);
        this.joins = joins;
        subscribe(access().insert(joins));
    }

    @Override
    public void onSuccess(List<Long> longs) {
        super.onSuccess(longs);
        for (TubeSong join : joins)
        if (context instanceof HomeView) ((HomeView) context).onTubeSongInserted(join.getTubeId());
    }
}
