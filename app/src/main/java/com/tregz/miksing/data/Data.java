package com.tregz.miksing.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public abstract class Data implements Parcelable {
    public final static String UNDEFINED = "Undefined";

    Data() {}

    Data(@NonNull String id) {
        this.id = id;
    }

    @PrimaryKey
    @NonNull
    private String id = UNDEFINED;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        String key = parcel.readString();
        id = key != null ? key : UNDEFINED;
    }

    protected void read(Parcel parcel) {
        parcel.writeString(id);
    }
}
