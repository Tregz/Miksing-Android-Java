package com.tregz.miksing.data.item.song;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.item.Item;

import java.util.Date;

@Entity(tableName = Song.TABLE)
public class Song extends Item {

    public static final String TABLE = "song";

    public Song(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @ColumnInfo(name = DataNotation.BD) private Date releasedAt;
    @ColumnInfo(name = DataNotation.FS) private String featuring;
    @ColumnInfo(name = DataNotation.LS) private String mixedBy;
    @ColumnInfo(name = DataNotation.MS) private String artist;
    private int what = 0;

    public String getArtist() {
        return artist;
    }

    public String getFeaturing() {
        return featuring;
    }

    public int getMix() {
        return what - (!isClean() ? 5 : 0);
    }

    public String getMixedBy() {
        return mixedBy;
    }

    public Date getReleasedAt() {
        return releasedAt;
    }

    public String getTitle() {
        return name;
    }

    public int getWhat() {
        return what;
    }

    public boolean isClean() {
        return what < What.UNDEFINED_DIRTY.ordinal();
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setClean(boolean value) {
        if (value && !isClean()) { what -= What.UNDEFINED_DIRTY.ordinal(); }
        else if (!value && isClean()) { what += What.UNDEFINED_DIRTY.ordinal(); }
    }

    public void setFeaturing(String featuring) {
        this.featuring = featuring;
    }

    public void setMix(int value) {
        what = value + (isClean() ? 5 : 0);
    }

    public void setMixedBy(String mixedBy) {
        this.mixedBy = mixedBy;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(artist);
        parcel.writeString(featuring);
        parcel.writeString(mixedBy);
        parcel.writeSerializable(releasedAt);
        parcel.writeInt(what);
        super.writeToParcel(parcel, i);
    }

    private Song(Parcel parcel) {
        artist = parcel.readString();
        featuring = parcel.readString();
        mixedBy = parcel.readString();
        releasedAt = (Date) parcel.readSerializable();
        what = parcel.readInt();
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel parcel) {
            return new Song(parcel);
        }

        @Override
        public Song[] newArray(int i) {
            return new Song[i];
        }
    };

    public enum What {
        UNDEFINED,
        MIX_CLEAN,
        EXTENDED_CLEAN,
        REMIX_CLEAN,
        REMIX_EXTENDED_CLEAN,
        UNDEFINED_DIRTY,
        MIX_DIRTY,
        EXTENDED_DIRTY,
        REMIX_DIRTY,
        REMIX_EXTENDED_DIRTY,
    }
}
