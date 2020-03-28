package com.tregz.miksing.data.song;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.DataObject;
import com.tregz.miksing.data.DataNotation;

import java.util.Date;

@Entity(tableName = Song.TABLE)
public class Song extends DataObject {

    public static final String TABLE = "song";

    public Song(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @ColumnInfo(name = DataNotation.AS) private String artist;
    @ColumnInfo(name = DataNotation.FS) private String featuring;
    @ColumnInfo(name = DataNotation.MS) private String mixedBy;
    @ColumnInfo(name = DataNotation.RD) private Date releasedAt;
    private int version = 0;

    public String getArtist() {
        return artist;
    }

    public String getFeaturing() {
        return featuring;
    }

    public int getMix() {
        return version - (!isClean() ? 5 : 0);
    } // TODO: update

    public String getMixedBy() {
        return mixedBy;
    }

    public Date getReleasedAt() {
        return releasedAt;
    }

    public String getTitle() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public boolean isClean() {
        return version < Version.UNDEFINED_DIRTY.ordinal();
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setClean(boolean value) {
        if (value && !isClean()) { version -= Version.UNDEFINED_DIRTY.ordinal(); }
        else if (!value && isClean()) { version += Version.UNDEFINED_DIRTY.ordinal(); }
    }

    public void setFeaturing(String featuring) {
        this.featuring = featuring;
    }

    public void setMix(int value) {
        version = value + (isClean() ? 5 : 0);
    } // TODO: update

    public void setMixedBy(String mixedBy) {
        this.mixedBy = mixedBy;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public void setVersion(int version) {
        this.version = version;
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
        parcel.writeInt(version);
        super.writeToParcel(parcel, i);
    }

    private Song(Parcel parcel) {
        artist = parcel.readString();
        featuring = parcel.readString();
        mixedBy = parcel.readString();
        releasedAt = (Date) parcel.readSerializable();
        version = parcel.readInt();
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

    public enum Version {
        // undefined mix version
        UNDEFINED(null, null), //0
        // undefined lyrics (clean or dirty)
        EDIT_UNDEFINED(Mix.EDIT, null), // 1
        EXTENDED_UNDEFINED(Mix.EXTENDED, null), // 2
        REMIX_UNDEFINED(Mix.REMIX, null), // 3
        REMIX_EXTENDED_UNDEFINED(Mix.EXTENDED_REMIX, null), // 4
        // clean lyrics
        UNDEFINED_CLEAN(null, Explicit.CLEAN),
        EDIT_CLEAN(Mix.EDIT, Explicit.CLEAN),
        EXTENDED_CLEAN(Mix.EXTENDED, Explicit.CLEAN),
        REMIX_CLEAN(Mix.REMIX, Explicit.CLEAN),
        REMIX_EXTENDED_CLEAN(Mix.EXTENDED_REMIX, Explicit.CLEAN),
        // dirty lyrics
        UNDEFINED_DIRTY(null, Explicit.DIRTY),
        EDIT_DIRTY(Mix.EDIT, Explicit.DIRTY),
        EXTENDED_DIRTY(Mix.EXTENDED, Explicit.DIRTY),
        REMIX_DIRTY(Mix.REMIX, Explicit.DIRTY),
        REMIX_EXTENDED_DIRTY(Mix.EXTENDED_REMIX, Explicit.DIRTY);

        private final Mix mix;
        private final Explicit explicit;

        public String getMix() {
            return mix != null ? capitalizeFully(mix.name()) : null;
        }

        public String getExplicit() {
            return explicit != null ? capitalizeFully(explicit.name()) : null;
        }

        private String capitalizeFully(String str) {
            return str.substring(0, 1).toUpperCase() + str.toLowerCase().substring(1);
        }

        Version(Mix mix, Explicit explicit) {
            this.mix = mix;
            this.explicit = explicit;
        }
    }

    public enum Mix {
        EDIT,
        EXTENDED,
        REMIX,
        EXTENDED_REMIX;
    }

    public enum Explicit {
        CLEAN,
        DIRTY
    }
}
