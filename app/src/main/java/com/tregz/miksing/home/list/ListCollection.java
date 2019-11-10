package com.tregz.miksing.home.list;

import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.tregz.miksing.data.Data;

import java.util.ArrayList;
import java.util.List;

public class ListCollection {
    private static final ListCollection sInstance = new ListCollection();

    public static ListCollection getInstance() {
        return sInstance;
    }

    private ListCollection() {
    }

    private final List<Data> list = new ArrayList<>();
    private final SparseArray<Data> map = new SparseArray<>();

    public int getListCount() {
        return list.size();
    }

    public void clear() {
        list.clear();
        map.clear();
    }

    List<Data> getList() {
        return list;
    }

    public Data add(@NonNull Data item) {
        list.add(item);
        map.put(map.size(), item);
        return item;
    }

    Data add(@NonNull Data item, int position) {
        map.put(position, item);
        return item;
    }

}
