package com.tregz.miksing.data.tube;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.tregz.miksing.data.Data;
import com.tregz.miksing.data.DataItem;
import com.tregz.miksing.data.DataJoin;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.user.User;

import java.util.Date;

@Entity(tableName = Tube.TABLE)
public class Tube extends DataItem {

    public Tube(@NonNull String id, @NonNull Date createdAt, @NonNull String name) {
        super(id, createdAt);
        this.name = name;
    }

    public final static String TABLE = "tube";

    @Override
    public int describeContents() {
        return 0;
    }

    private Tube(Parcel parcel) {
        read(parcel);
    }

    public static final Creator CREATOR = new Creator<Tube>() {
        @Override
        public Tube createFromParcel(Parcel parcel) {
            return new Tube(parcel);
        }

        @Override
        public Tube[] newArray(int i) {
            return new Tube[i];
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
