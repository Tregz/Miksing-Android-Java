package com.tregz.miksing.data.tube.song;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.DataJoin;
import com.tregz.miksing.data.user.tube.UserTube;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = TubeSong.TABLE, foreignKeys = {@ForeignKey(entity = Song.class,
        onDelete = CASCADE, onUpdate = CASCADE,
        childColumns = Song.TABLE, parentColumns = DataNotation.PK)},
        indices = {@Index(Song.TABLE)})
public class TubeSong extends DataJoin {

    public TubeSong(@NonNull String tubeId, @NonNull String songId) {
        setId(tubeId + "-" + songId);
        this.songId = songId;
        this.tubeId = tubeId;
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

    public @NonNull
    String getTubeId() {
        return tubeId;
    }

    public void setTubeId(@NonNull String tubeId) {
        this.tubeId = tubeId;
    }

    @NonNull
    @ColumnInfo(name = UserTube.TABLE)
    private String tubeId = "Undefined";

    public final static String TABLE = "tube_song";

    @Override
    public int describeContents() {
        return 0;
    }

    private TubeSong(Parcel parcel) {
        read(parcel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<TubeSong>() {
        @Override
        public TubeSong createFromParcel(Parcel parcel) {
            return new TubeSong(parcel);
        }

        @Override
        public TubeSong[] newArray(int i) {
            return new TubeSong[i];
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