package com.tregz.miksing.data.user.list.song;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.song.Song;

public class ListSongRelation {

    @Embedded public ListSong join;

    @Relation(parentColumn = Song.TABLE, entityColumn = DataNotation.PK) public Song song;

    //@Relation(parentColumn = User.TABLE, entityColumn = DataNotation.PK) public User user;

}
