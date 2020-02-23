package com.tregz.miksing.data.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.DataItem;

import java.util.Date;


@Entity(tableName = User.TABLE)
public class User extends DataItem {

    public final static String TABLE = "user";

    @ColumnInfo(name = DataNotation.BD)
    private Date birthDay;

    @ColumnInfo(name = DataNotation.AS)
    private String email;

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
