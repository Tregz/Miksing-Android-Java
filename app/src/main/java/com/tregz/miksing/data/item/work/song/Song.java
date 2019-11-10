package com.tregz.miksing.data.item.work.song;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.tregz.miksing.data.item.work.Work;

import java.util.Date;

@Entity(tableName = Song.TABLE)
public class Song extends Work {
    final static String TABLE = "song";
    public static final String MIXED_BY = SongField.JACK.getName();

    public Song(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    private String mixedBy;
    
    public int getMix() {
        return kind - (getDirty() ? 5 : 0);
    }

    public void setMix(int value) {
        kind = value + (getDirty() ? 5 : 0);
    }

    public boolean getDirty() {
        return kind >= Kind.UNDEFINED_DIRTY.ordinal();
    }

    public void setDirty(boolean value) {
        if (value && !getDirty()) { kind += Kind.UNDEFINED_DIRTY.ordinal(); }
        else if (!value && getDirty()) { kind -= Kind.UNDEFINED_DIRTY.ordinal(); }
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

    protected enum SongField {
        JACK("jack");

        private String name;

        public String getName() {
            return name;
        }

        SongField(String name) { this.name = name; }
    }

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
