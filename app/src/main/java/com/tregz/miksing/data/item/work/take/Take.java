package com.tregz.miksing.data.item.work.take;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.tregz.miksing.data.item.work.Work;

import java.util.Date;

public class Take extends Work {

    public Take(@NonNull String id, @NonNull Date createAt) {
        super(id, createAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    private Take(Parcel parcel) {
        read(parcel);
    }

    public static final Creator CREATOR = new Creator<Take>() {
        @Override
        public Take createFromParcel(Parcel parcel) {
            return new Take(parcel);
        }

        @Override
        public Take[] newArray(int i) {
            return new Take[i];
        }
    };
}
