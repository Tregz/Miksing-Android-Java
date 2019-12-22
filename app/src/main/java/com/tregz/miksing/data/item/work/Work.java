package com.tregz.miksing.data.item.work;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.item.Item;

import java.util.Date;

public abstract class Work extends Item {

    protected Work() {}

    protected Work(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @ColumnInfo(name = DataNotation.BD)
    private Date releasedAt;
    @ColumnInfo(name = DataNotation.MS)
    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    protected int what = 0;

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        releasedAt = (Date) parcel.readSerializable();
        artist = parcel.readString();
        what = parcel.readInt();
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        parcel.writeSerializable(releasedAt);
        parcel.writeString(artist);
        parcel.writeInt(what);
        super.read(parcel);
    }
}
