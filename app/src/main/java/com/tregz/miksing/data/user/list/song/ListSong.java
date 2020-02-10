package com.tregz.miksing.data.user.list.song;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.DataJoin;
import com.tregz.miksing.data.user.list.UserList;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = ListSong.TABLE, foreignKeys = {
        @ForeignKey(entity = Song.class, onDelete = CASCADE, onUpdate = CASCADE,
                childColumns = Song.TABLE, parentColumns = "id")})
public class ListSong extends DataJoin {

    public ListSong(@NonNull String listId, @NonNull String songId) {
        setId(listId + "-" + songId);
        this.songId = songId;
        this.listId = listId;
    }

    @NonNull
    public String getSongId() {
        return songId;
    }

    public void setSongId(@NonNull String songId) {
        this.songId = songId;
    }

    @NonNull
    @ColumnInfo(name = Song.TABLE)
    private String songId = "Undefined";

    public @NonNull String getListId() {
        return listId;
    }

    public void setListId(@NonNull String listId) {
        this.listId = listId;
    }

    @NonNull
    @ColumnInfo(name = UserList.TABLE)
    private String listId = "Undefined";

    public final static String TABLE = "list_song";

    @Override
    public int describeContents() {
        return 0;
    }

    private ListSong(Parcel parcel) {
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ListSong>() {
        @Override
        public ListSong createFromParcel(Parcel parcel) {
            return new ListSong(parcel);
        }

        @Override
        public ListSong[] newArray(int i) {
            return new ListSong[i];
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