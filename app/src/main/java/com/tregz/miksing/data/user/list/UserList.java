package com.tregz.miksing.data.user.list;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.DataJoin;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.user.User;

@Entity(tableName = UserList.TABLE)
public class UserList extends DataJoin {

    public UserList(@NonNull String userId, @NonNull String name) {
        setId(userId + "-" + name);
        this.userId = userId;
        this.name = name;
    }

    public @NonNull String getListId() {
        return userId;
    }

    public void seUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public @NonNull String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    @ColumnInfo(name = User.TABLE)
    private String userId = "Undefined";

    @ColumnInfo(name = DataNotation.NS)
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final static String TABLE = "user_list";

    @Override
    public int describeContents() {
        return 0;
    }

    private UserList(Parcel parcel) {
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<UserList>() {
        @Override
        public UserList createFromParcel(Parcel parcel) {
            return new UserList(parcel);
        }

        @Override
        public UserList[] newArray(int i) {
            return new UserList[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        super.read(parcel);
    }
}
