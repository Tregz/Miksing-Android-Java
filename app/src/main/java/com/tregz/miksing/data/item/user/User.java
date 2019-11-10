package com.tregz.miksing.data.item.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.tregz.miksing.data.item.Item;

import java.util.Date;


@Entity(tableName = User.TABLE)
public class User extends Item {
    final static String TABLE = "user";

    public Date getBirthDay() {
        return born;
    }

    public void setBirthDay(Date birthDay) {
        this.born = birthDay;
    }

    public User(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    private User(Parcel parcel) {
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };

}
