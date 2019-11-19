package com.tregz.miksing.data;

import androidx.sqlite.db.SimpleSQLiteQuery;

import io.reactivex.schedulers.Schedulers;

public class DataQuery<T> {

    DataAccess access;
    String table;

    public DataQuery(DataAccess access, String table) {
        this.access = access;
        this.table = table;
    }

}
