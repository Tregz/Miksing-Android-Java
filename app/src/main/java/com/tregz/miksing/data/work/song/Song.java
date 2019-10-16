package com.tregz.miksing.data.work.song;

import android.os.Parcel;
import android.os.Parcelable;

import com.tregz.miksing.data.work.Work;

public class Song extends Work {

    private String mixedBy;
    private int kind = 0;

    public Song() {}

    public String getMixedBy() {
        return mixedBy;
    }

    public void setMixedBy(String mixedBy) {
        this.mixedBy = mixedBy;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //TODO
    }

    private Song(Parcel parcel) {
        //TODO
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
