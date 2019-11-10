package com.tregz.miksing.data.item;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.tregz.miksing.data.Data;

import java.util.Date;

public abstract class Item extends Data {
    private static final String COLUMN_BORN = "born";
    private static final String COLUMN_MARK = "mark";
    private static final String COLUMN_NAME = "name";

    protected Item(@NonNull String id, @NonNull Date createdAt) {
        super(id, createdAt);
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    @ColumnInfo(name = COLUMN_BORN) protected Date born;
    @ColumnInfo(name = COLUMN_MARK) protected String mark;
    @ColumnInfo(name = COLUMN_NAME) protected String name;

    protected Item() {}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        mark = parcel.readString();
        name = parcel.readString();
        born = (Date) parcel.readSerializable();
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void read(Parcel parcel) {
        parcel.writeSerializable(born);
        parcel.writeString(mark);
        parcel.writeString(name);
        super.read(parcel);
    }

    protected enum ItemField {
        BORN(COLUMN_BORN),
        MARK(COLUMN_MARK),
        NAME(COLUMN_NAME);

        private String name;

        public String getName() {
            return name;
        }

        ItemField(String name) { this.name = name; }
    }
}
