package com.tregz.miksing.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.Date;

public abstract class Data implements Parcelable {

    protected Data() {
    }

    protected Data(@NonNull String id, @NonNull Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    @PrimaryKey
    @NonNull
    private String id = "Undefined";
    @NonNull
    @ColumnInfo(name = DataNotation.C)
    private Date createdAt = new Date();
    @ColumnInfo(name = DataNotation.E)
    private Date updatedAt;

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        String key = parcel.readString();
        id = key != null ? key : "Undefined";
        Date copy = (Date) parcel.readSerializable();
        createdAt = copy != null ? copy : new Date();
        updatedAt = (Date) parcel.readSerializable();
    }

    protected void read(Parcel parcel) {
        parcel.writeString(id);
        parcel.writeSerializable(createdAt);
        parcel.writeSerializable(updatedAt);
    }
}
