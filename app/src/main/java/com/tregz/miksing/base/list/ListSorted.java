package com.tregz.miksing.base.list;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongRelation;
import com.tregz.miksing.data.user.tube.UserTubeRelation;

import java.util.Date;

public abstract class ListSorted<T> extends SortedListAdapterCallback<T> {
    protected String TAG = ListSorted.class.getSimpleName();

    public static Order comparator = Order.ALPHA;

    public static boolean customOrder() {
        return comparator == Order.DIGIT;
    }

    public ListSorted(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    protected int compare(String s1, String s2) {
        return s1 != null && s2 != null ? s1.toLowerCase().compareTo(s2.toLowerCase()) : -1;
    }

    protected int compare(Date d1, Date d2) {
        return d1 != null && d2 != null ? d1.compareTo(d2) : -1;
    }

    public enum Order {
        ALPHA,
        DIGIT,
        FRESH
    }
}
