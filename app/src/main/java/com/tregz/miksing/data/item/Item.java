package com.tregz.miksing.data.item;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.tregz.miksing.data.Data;

import java.util.Date;

public abstract class Item extends Data {
    private static final String BORN = "born";
    private static final String MARK = "mark";
    private static final String NAME = "name";
    public static String BORN_SINCE = "bornSince";
    public static String MARK_BRAND = "markBrand";
    public static String NAME_GIVEN = "nameGiven";

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

    @ColumnInfo(name = BORN) protected Date born;
    @ColumnInfo(name = MARK) protected String mark;
    @ColumnInfo(name = NAME) protected String name;

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
}
