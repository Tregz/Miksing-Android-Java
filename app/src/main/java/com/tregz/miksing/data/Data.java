package com.tregz.miksing.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.Date;

public abstract class Data implements Parcelable {

    Data() {}

    Data(@NonNull String id) {
        this.id = id;
    }

    @PrimaryKey
    @NonNull
    private String id = "Undefined";

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
        id = key != null ? key : "Undefined";
    }

    protected void read(Parcel parcel) {
        parcel.writeString(id);
    }
}
