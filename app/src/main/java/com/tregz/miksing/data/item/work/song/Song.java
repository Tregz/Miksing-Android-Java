package com.tregz.miksing.data.item.work.song;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.item.work.Work;

import java.util.Date;

@Entity(tableName = Song.TABLE)
public class Song extends Work {

    public static final String TABLE = "song";

    public Song(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @ColumnInfo(name = DataNotation.L) private String mixedBy;
    @ColumnInfo(name = DataNotation.F) private String featuring;

    public String getFeaturing() {
        return featuring;
    }

    public void setFeaturing(String featuring) {
        this.featuring = featuring;
    }

    public int getMix() {
        return what - (!isClean() ? 5 : 0);
    }

    public void setMix(int value) {
        what = value + (isClean() ? 5 : 0);
    }

    public boolean isClean() {
        return what < What.UNDEFINED_DIRTY.ordinal();
    }

    public void setClean(boolean value) {
        if (value && !isClean()) { what -= What.UNDEFINED_DIRTY.ordinal(); }
        else if (!value && isClean()) { what += What.UNDEFINED_DIRTY.ordinal(); }
    }
    
    public String getMixedBy() {
        return mixedBy;
    }

    public void setMixedBy(String mixedBy) {
        this.mixedBy = mixedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mixedBy);
        parcel.writeInt(what);
        super.writeToParcel(parcel, i);
    }

    private Song(Parcel parcel) {
        mixedBy = parcel.readString();
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
