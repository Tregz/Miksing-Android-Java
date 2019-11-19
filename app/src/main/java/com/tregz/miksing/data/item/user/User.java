package com.tregz.miksing.data.item.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.item.Item;

import java.util.Date;


@Entity(tableName = User.TABLE)
public class User extends Item {

    final static String TABLE = "user";

    @ColumnInfo(name = DataNotation.B)
    private Date birthDay;

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getFirstName() {
        return name;
    }

    public void setFirstName(String name) {
        this.name = name;
    }

    public User(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        birthDay = (Date) parcel.readSerializable();
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        parcel.writeSerializable(birthDay);
        super.read(parcel);
    }

}
