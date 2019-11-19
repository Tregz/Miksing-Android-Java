package com.tregz.miksing.data.join.work.song.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.tregz.miksing.data.item.work.song.Song;
import com.tregz.miksing.data.join.Join;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = UserSong.TABLE, foreignKeys = @ForeignKey(
        entity = Song.class, onDelete = CASCADE, onUpdate = CASCADE,
        childColumns = "song_id", parentColumns = "id"))
public class UserSong extends Join {

    public UserSong(@NonNull String songId) {
        this.songId = songId;
    }

    @NonNull
    public String getSongId() {
        return songId;
    }

    public void setSongId(@NonNull String songId) {
        this.songId = songId;
    }

    @PrimaryKey @NonNull
    @ColumnInfo(name = "song_id") private String songId = "Undefined";

    public final static String TABLE = "user_song";


    @Override
    public int describeContents() {
        return 0;
    }

    private UserSong(Parcel parcel) {
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<UserSong>() {
        @Override
        public UserSong createFromParcel(Parcel parcel) {
            return new UserSong(parcel);
        }

        @Override
        public UserSong[] newArray(int i) {
            return new UserSong[i];
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