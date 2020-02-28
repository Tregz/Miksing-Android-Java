package com.tregz.miksing.home.list.song;

import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.tube.song.TubeSong;
import com.tregz.miksing.data.tube.song.TubeSongRelation;

class SongListSorted<T> extends ListSorted<T> {

    SongListSorted(RecyclerView.Adapter adapter) {
        super(adapter);
    }


    @Override
    public boolean areContentsTheSame(T o1, T o2) {
        if (o1 instanceof TubeSongRelation) {
            Song song1 = ((TubeSongRelation) o1).song;
            Song song2 = ((TubeSongRelation) o2).song;
            if (song1 != null && song2 != null) return song1.getUpdatedAt() == song2.getUpdatedAt();
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

}
