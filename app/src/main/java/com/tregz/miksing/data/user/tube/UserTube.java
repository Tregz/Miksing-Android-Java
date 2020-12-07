package com.tregz.miksing.data.user.tube;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.tregz.miksing.data.DataPositionable;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.user.User;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = UserTube.TABLE, foreignKeys = {
        @ForeignKey(entity = User.class,
                onDelete = CASCADE, onUpdate = CASCADE,
                childColumns = User.TABLE, parentColumns = DataNotation.ID),
        @ForeignKey(entity = Tube.class,
                onDelete = CASCADE, onUpdate = CASCADE,
                childColumns = Tube.TABLE, parentColumns = DataNotation.ID)
}, indices = {@Index(User.TABLE), @Index(Tube.TABLE)})
public class UserTube extends DataPositionable {

    public UserTube(@NonNull String userId, @NonNull String tubeId) {
        setId(userId + "-" + tubeId);
        this.userId = userId;
        this.tubeId = tubeId;
    }

    public @NonNull
    String getTubeId() {
        return tubeId;
    }

    public void setTubeId(@NonNull String tubeId) {
        this.tubeId = tubeId;
    }

    public @NonNull
    String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    @ColumnInfo(name = User.TABLE)
    private String userId = "Undefined";

    @NonNull
    @ColumnInfo(name = Tube.TABLE)
    private String tubeId = "Undefined";

    public final static String TABLE = "user_tube";

    @Override
    public int describeContents() {
        return 0;
    }

    private UserTube(Parcel parcel) {
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<UserTube>() {
        @Override
        public UserTube createFromParcel(Parcel parcel) {
            return new UserTube(parcel);
        }

        @Override
        public UserTube[] newArray(int i) {
            return new UserTube[i];
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
