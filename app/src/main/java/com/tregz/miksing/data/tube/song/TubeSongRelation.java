package com.tregz.miksing.data.tube.song;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.user.User;

public class TubeSongRelation {
    @Embedded
    public TubeSong join;
    @Relation(parentColumn = Song.TABLE, entityColumn = DataNotation.PK)
    public Song song;
    /* @Relation(parentColumn = User.TABLE, entityColumn = DataNotation.PK)
    public User user; */
}
