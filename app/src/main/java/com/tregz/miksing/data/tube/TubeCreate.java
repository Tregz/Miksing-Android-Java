package com.tregz.miksing.data.tube;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tregz.miksing.BuildConfig;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.song.SongListener;

import java.util.Date;

public class TubeCreate extends DataMaybe<Tube> implements TubeInsert.OnSave {
    private final String TAG = TubeCreate.class.getSimpleName();

    private final Context context;
    private TubeAccess access;
    private final String id;
    private final String name;
    private final Tube tube;
    private final TubeCreate.OnUserSongListener listener;

    public TubeCreate(
            @NonNull Context context,
            @NonNull String id,
            @Nullable String name
    ) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.tube = null;
        this.listener = null;
        subscribe(access().whereId(id));
    }

    public TubeCreate(
            @NonNull Context context,
            @NonNull String id,
            @Nullable String name,
            @Nullable Tube tube,
            TubeCreate.OnUserSongListener listener
    ) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.tube = tube;
        this.listener = listener;
        subscribe(access().whereId(id));
    }

    @Override
    public void onComplete() {
        String name = this.name != null ? this.name : "tx_tube_default";
        Tube tube = this.tube != null ? this.tube : new Tube(id, new Date(), name);
        new TubeInsert(context, tube, this);
    }

    @Override
    public void onSuccess(Tube tube) {
        saved();
    }

    @Override
    public void saved() {
        if (BuildConfig.DEBUG && id.equals(AuthUtil.userId()))
            Log.d(TAG, "User's main list inserted");
        if (listener != null) listener.onUserSongListener();
    }

    private TubeAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTube();
        return access;
    }

    public interface OnUserSongListener {
        void onUserSongListener();
    }
}
