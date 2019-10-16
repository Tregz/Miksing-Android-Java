package com.tregz.miksing.data.work;

import android.os.Parcel;

import com.tregz.miksing.data.DataModel;

import java.util.Date;

public abstract class Work extends DataModel {

    private String mark;
    private String name;

    public String getArtist() {
        return mark;
    }

    public void setArtist(String mark) {
        this.mark = mark;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public Date getReleasedAt() {
        return born;
    }

    public void setReleasedAt(Date born) {
        this.born = born;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        mark = parcel.readString();
        name = parcel.readString();
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        parcel.writeString(mark);
        parcel.writeString(name);
        super.read(parcel);
    }
}
