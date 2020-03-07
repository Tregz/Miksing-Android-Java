package com.tregz.miksing.data.tube.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;

public class TubeSongWrite extends DataMaybe<TubeSong> {
    private String TAG = TubeSongWrite.class.getSimpleName();

    private TubeSongAccess access;
    private TubeSong join;
    private Context context;

    public TubeSongWrite(Context context, TubeSong join) {
        this.context = context;
        this.join = join;
        subscribe(access().whereId(join.getId()));
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
        new TubeSongInsert(context, access().insert(join));
    }

    @Override
    public void onSuccess(TubeSong tubeSong) {
        Log.d(TAG, "onSuccess");
        new DataUpdate(access().update(join));
    }

    private TubeSongAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTubeSong();
        return access;
    }
}
