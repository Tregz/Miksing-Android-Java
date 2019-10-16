package com.tregz.miksing.data.work.take;

import android.os.Parcel;

import com.tregz.miksing.data.work.Work;

public class Take extends Work {

    public Take() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //TODO
    }

    private Take(Parcel parcel) {
        //TODO
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
