package com.tregz.miksing.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public abstract class DataModel implements Parcelable {

    public String getKey() {
        return _key;
    }

    public void setKey(String _key) {
        this._key = _key;
    }

    private String _key;
    protected Date born;
    protected Date copy;

    public Date getCreatedAt() {
        return copy;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        _key = parcel.readString();
        born = (Date) parcel.readSerializable();
        copy = (Date) parcel.readSerializable();
    }

    protected void read(Parcel parcel) {
        parcel.writeString(_key);
        parcel.writeSerializable(born);
        parcel.writeSerializable(copy);
    }
}
