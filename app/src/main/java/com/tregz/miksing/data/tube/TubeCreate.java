package com.tregz.miksing.data.tube;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tregz.miksing.data.DataMaybe;
import com.tregz.miksing.data.DataReference;

import java.util.Date;

public class TubeCreate extends DataMaybe<Tube> implements TubeInsert.OnSave {
    private final String TAG = TubeCreate.class.getSimpleName();

    private Context context;
    private TubeAccess access;
    private String id;
    private String name;

    public TubeCreate(
            @NonNull Context context,
            @NonNull String id,
            @Nullable String name
    ) {
        this.context = context;
        this.id = id;
        this.name = name;
        subscribe(access().whereId(id));
    }

    @Override
    public void onComplete() {
        String name = this.name != null ? this.name : "tx_tube_default";
        new TubeInsert(context, new Tube(id, new Date(), name), this);
    }

    @Override
    public void onSuccess(Tube tube) {
        saved();
    }

    @Override
    public void saved() {
        //
    }

    private TubeAccess access() {
        if (access == null) access = DataReference.getInstance(context).accessTube();
        return access;
    }
}
