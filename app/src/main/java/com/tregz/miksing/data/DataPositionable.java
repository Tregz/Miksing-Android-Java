package com.tregz.miksing.data;

import android.os.Parcel;

import androidx.room.ColumnInfo;

public abstract class DataPositionable extends Data {

    protected DataPositionable() {}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @ColumnInfo(name = DataNotation.PI)
    private int position;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        position = parcel.readInt();
    }

    protected void read(Parcel parcel) {
        parcel.writeInt(position);
    }
}
