package com.tregz.miksing.data.tube.song;

import android.content.Context;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;

public class TubeSongSaver extends DataMaybe<TubeSong> {

    private TubeSongAccess access;
    private TubeSong join;
    private Context context;

    public TubeSongSaver(Context context, TubeSong join) {
        this.context = context;
        this.join = join;
        subscribe(access().whereId(join.getId()));
    }

    @Override
    public void onComplete() {
        new TubeSongInsert(access().insert(join), join);
    }

    @Override
    public void onSuccess(TubeSong tubeSong) {
        new DataUpdate(access().update(join));
    }

    private TubeSongAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTubeSong();
        return access;
    }
}
