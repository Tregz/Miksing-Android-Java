package com.tregz.miksing.home.work;

import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.tregz.miksing.data.DataModel;
import com.tregz.miksing.data.work.Work;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkCollection {
    private static final WorkCollection sInstance = new WorkCollection();

    public static WorkCollection getInstance() {
        return sInstance;
    }

    private WorkCollection() {
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

    public List<Work> getList() {
        return list;
    }

    Work add(@NonNull Work work) {
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
