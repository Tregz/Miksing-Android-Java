package com.tregz.miksing.data.lang;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.tregz.miksing.data.DataObject;

import java.util.Date;

@Entity(tableName = Lang.TABLE)
public class Lang extends DataObject {

    public final static String TABLE = "lang";

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getFrench() {
        return french;
    }

    public void setFrench(String french) {
        this.french = french;
    }

    private String english;
    private String french;

    public Lang(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Lang(Parcel parcel) {
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Lang>() {
        @Override
        public Lang createFromParcel(Parcel parcel) {
            return new Lang(parcel);
        }

        @Override
        public Lang[] newArray(int i) {
            return new Lang[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        english = parcel.readString();
        french = parcel.readString();
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        parcel.writeString(english);
        parcel.writeString(french);
        super.read(parcel);
    }
}
