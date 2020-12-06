package com.tregz.miksing.data.tube.song;

import android.content.Context;
import android.util.Log;

import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;

public class TubeSongWrite extends DataMaybe<TubeSong> {
    private final String TAG = TubeSongWrite.class.getSimpleName();

    private TubeSongAccess access;
    private final TubeSong join;
    private final Context context;

    public TubeSongWrite(Context context, TubeSong join) {
        this.context = context;
        this.join = join;
        subscribe(access().whereId(join.getId()));
    }

    @Override
    public void onComplete() {
        if (BuildConfig.DEBUG) Log.d(TAG, "onComplete: " + join.getTubeId());
        // TODO FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY)
        new TubeSongInsert(context, access().insert(join));
    }

    @Override
    public void onSuccess(TubeSong tubeSong) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onSuccess");
        new DataUpdate(access().update(join));
    }

    private TubeSongAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTubeSong();
        return access;
    }
}
