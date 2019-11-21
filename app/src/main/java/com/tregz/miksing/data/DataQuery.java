package com.tregz.miksing.data;

public class DataQuery<T> {

    DataAccess access;
    String table;

    public DataQuery(DataAccess access, String table) {
        this.access = access;
        this.table = table;
    }

}
