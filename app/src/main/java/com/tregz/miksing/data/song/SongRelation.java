package com.tregz.miksing.data.song;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.tube.song.TubeSong;

public class SongRelation {
    @Embedded
    public Song song;
    @Relation(parentColumn = DataNotation.PK, entityColumn = Song.TABLE)
    public TubeSong join;
    /* @Relation(parentColumn = User.TABLE, entityColumn = DataNotation.PK)
    public User user; */
}
