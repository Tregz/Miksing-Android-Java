package com.tregz.miksing.data.tube.song;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;

import java.util.List;

public class TubeSongQuery extends DataMaybe<List<TubeSongRelation>> {
    private String TAG = TubeSongQuery.class.getSimpleName();

    private OnTubeSongQueryDataResultCallback listener;
    private TubeSongAccess access;
    private String newTubeId;
    private String tubeId;
    private Context context;

    public TubeSongQuery(
            @NonNull Context context,
            @NonNull OnTubeSongQueryDataResultCallback listener,
            @NonNull String tubeId
    ) {
        this.context = context;
        this.listener = listener;
        this.tubeId = tubeId;
        access = DataReference.getInstance(context).accessTubeSong();
    }

    public void copyFromTube(String newTubeId) {
        this.newTubeId = newTubeId;
        subscribe(access.whereTube(tubeId));
    }

    /* public LiveData<TubeSong> query(Context context, String id) {
        String request = "SELECT * FROM " + TubeSong.TABLE + " WHERE id = '" + id + "'";
        TubeSongAccess access = DataReference.getInstance(context).accessTubeSong();
        return access.item(new SimpleSQLiteQuery(request));
    } */

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete " + tubeId);
    }

    @Override
    public void onSuccess(List<TubeSongRelation> relations) {
        Log.d(TAG, "onSuccess " + tubeId);
        if (newTubeId != null) {
            listener.onTubeSongQueryDataResult(relations, newTubeId);
            DataReference.getInstance(context).accessTube().delete(tubeId);
        }
    }

    public interface OnTubeSongQueryDataResultCallback {
        void onTubeSongQueryDataResult(List<TubeSongRelation> relations, String newTubeId);
    }
}
