package com.tregz.miksing.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.Date;

public abstract class Data implements Parcelable {
    private static final String _KEY = "_key";
    private static final String COPY = "copy";
    public static final String PRIMARY_ID = "primaryId";
    public static final String CREATED_AT = "createdAt";

    protected Data(@NonNull String id, @NonNull Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    protected Data() {}

    @PrimaryKey @NonNull @ColumnInfo(name = _KEY) private String id = "Undefined";
    @ColumnInfo(name = COPY) private Date createdAt;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        String key = parcel.readString();
        id = key != null ? key : "Undefined";
        createdAt = (Date) parcel.readSerializable();
    }

    protected void read(Parcel parcel) {
        parcel.writeString(id);
        parcel.writeSerializable(createdAt);
    }
}
