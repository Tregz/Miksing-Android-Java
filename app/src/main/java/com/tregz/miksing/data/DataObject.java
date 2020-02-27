package com.tregz.miksing.data;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import java.util.Date;

public abstract class DataObject extends Data {

    protected DataObject() {
    }

    protected DataObject(@NonNull String id, @NonNull Date createdAt) {
        super(id);
        this.createdAt = createdAt;
    }

    @NonNull
    @ColumnInfo(name = DataNotation.CD)
    private Date createdAt = new Date();
    @ColumnInfo(name = DataNotation.DD)
    private Date deletedAt;
    @ColumnInfo(name = DataNotation.NS)
    protected String name;
    @ColumnInfo(name = DataNotation.ED)
    private Date updatedAt;

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Date copy = (Date) parcel.readSerializable();
        createdAt = copy != null ? copy : new Date();
        deletedAt = (Date) parcel.readSerializable();
        name = parcel.readString();
        updatedAt = (Date) parcel.readSerializable();
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        parcel.writeSerializable(createdAt);
        parcel.writeSerializable(deletedAt);
        parcel.writeString(name);
        parcel.writeSerializable(updatedAt);
        super.read(parcel);
    }
}
