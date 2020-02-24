package com.tregz.miksing.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import com.tregz.miksing.data.DataNotation;

public abstract class DataPositionable extends Data {

    protected DataPositionable() {}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @ColumnInfo(name = DataNotation.SI)
    private int position;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        position = parcel.readInt();
    }

    protected void read(Parcel parcel) {
        parcel.writeInt(position);
    }
}
