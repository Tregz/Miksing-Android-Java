package com.tregz.miksing.base.list;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongRelation;

import java.util.Date;

public class ListSorted<T> extends SortedListAdapterCallback<T> {
    //private String TAG = ListSorted.class.getSimpleName();

    public static Order comparator = Order.ALPHA;

    public static boolean customOrder() {
        return comparator == Order.DIGIT;
    }

    public ListSorted(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public boolean areContentsTheSame(T o1, T o2) {
        if (o1 instanceof TubeSongRelation) {
            Song song1 = ((TubeSongRelation) o1).song;
            Song song2 = ((TubeSongRelation) o2).song;
            if (song1 != null && song2 != null)
                return song1.getUpdatedAt() == song2.getUpdatedAt();
        }
        return false;
    }

    @Override
    public boolean areItemsTheSame(T o1, T o2) {
        if (o1 instanceof TubeSongRelation) {
            Song song1 = ((TubeSongRelation) o1).song;
            Song song2 = ((TubeSongRelation) o2).song;
            if (song1 != null && song2 != null)
                return compare(song1.getId(), song2.getId()) == 0;
        }
        return false;
    }

    @Override
    public int compare(T o1, T o2) {
        switch (comparator) {
            case ALPHA:
                if (o1 instanceof TubeSongRelation) {
                    Song song1 = ((TubeSongRelation) o1).song;
                    Song song2 = ((TubeSongRelation) o2).song;
                    if (song1 != null && song2 != null)
                        return compare(song1.getName(), song2.getName());
                }
            case DIGIT:
                if (o1 instanceof TubeSongRelation) {
                    TubeSong join1 = ((TubeSongRelation) o1).join;
                    TubeSong join2 = ((TubeSongRelation) o2).join;
                    if (join1 != null && join2 != null)
                        return join1.getPosition() - join2.getPosition();
                }
            case FRESH:
                if (o1 instanceof TubeSongRelation) {
                    Song song1 = ((TubeSongRelation) o1).song;
                    Song song2 = ((TubeSongRelation) o2).song;
                    if (song1 != null && song2 != null)
                        return compare(song1.getReleasedAt(), song2.getReleasedAt());
                }
        }
        return 0;
    }

    private int compare(String s1, String s2) {
        return s1 != null && s2 != null ? s1.toLowerCase().compareTo(s2.toLowerCase()) : -1;
    }

    private int compare(Date d1, Date d2) {
        return d1 != null && d2 != null ? d1.compareTo(d2) : -1;
    }

    public enum Order {
        ALPHA,
        DIGIT,
        FRESH
    }
}
