package com.tregz.miksing.data.item;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.tregz.miksing.data.Data;
import com.tregz.miksing.data.DataNotation;

import java.util.Date;

public abstract class Item extends Data {

    protected Item() {}

    protected Item(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @ColumnInfo(name = DataNotation.DD)
    private Date deletedAt;
    @ColumnInfo(name = DataNotation.NS)
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        deletedAt = (Date) parcel.readSerializable();
        name = parcel.readString();
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        parcel.writeSerializable(deletedAt);
        parcel.writeString(name);
        super.read(parcel);
    }
}
