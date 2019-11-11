package com.tregz.miksing.data.item.work.song;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.item.work.Work;

import java.util.Date;

@Entity(tableName = Song.TABLE)
public class Song extends Work {
    final static String TABLE = "song";
    private static final String PROD = "prod";
    public static final String PROD_MAKER = "mixedBy"; // producer
    public static final String RADIO_EDIT = "clean"; // edited out
    public static final String MIX_RECORD = "version"; // recording

    public Song(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @ColumnInfo(name = PROD) private String mixedBy;
    
    public int getMix() {
        return kind - (isDirty() ? 5 : 0);
    }

    public void setMix(int value) {
        kind = value + (isDirty() ? 5 : 0);
    }

    public boolean isDirty() {
        return kind >= Kind.UNDEFINED_DIRTY.ordinal();
    }

    public void setDirty(boolean value) {
        if (value && !isDirty()) { kind += Kind.UNDEFINED_DIRTY.ordinal(); }
        else if (!value && isDirty()) { kind -= Kind.UNDEFINED_DIRTY.ordinal(); }
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
        parcel.writeInt(kind);
        super.writeToParcel(parcel, i);
    }

    private Song(Parcel parcel) {
        mixedBy = parcel.readString();
        kind = parcel.readInt();
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

    private enum Kind {
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
