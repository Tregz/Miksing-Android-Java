package com.tregz.miksing.home.list;

import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.tregz.miksing.data.work.Work;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListCollection {
    private static final ListCollection sInstance = new ListCollection();

    public static ListCollection getInstance() {
        return sInstance;
    }

    private ListCollection() {
    }

    private final List<Work> list = new ArrayList<>();
    private final Set<Work> set = new HashSet<>();
    private final SparseArray<Work> map = new SparseArray<>();

    public int getListCount() {
        return list.size();
    }

    public int getSetCount() {
        return set.size();
    }

    public void clear() {
        list.clear();
        set.clear();
        map.clear();
    }

    List<Work> getList() {
        return list;
    }

    public Work add(@NonNull Work work) {
        list.add(work);
        set.add(work);
        map.put(map.size(), work);
        return work;
    }

    Work add(@NonNull Work work, int position) {
        map.put(position, work);
        return work;
    }

}
