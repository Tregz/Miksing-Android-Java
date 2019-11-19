package com.tregz.miksing.data.join;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import com.tregz.miksing.data.DataNotation;

public abstract class Join implements Parcelable {

    protected Join() {}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @ColumnInfo(name = DataNotation.S)
    private int position;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        position = parcel.readInt();
    }

    protected void read(Parcel parcel) {
        parcel.writeInt(position);
    }
}
