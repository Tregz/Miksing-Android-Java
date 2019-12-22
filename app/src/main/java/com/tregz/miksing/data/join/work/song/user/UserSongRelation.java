package com.tregz.miksing.data.join.work.song.user;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.item.user.User;
import com.tregz.miksing.data.item.work.song.Song;

public class UserSongRelation {

    @Embedded public UserSong join;

    @Relation(parentColumn = Song.TABLE, entityColumn = DataNotation.PK) public Song song;

    //@Relation(parentColumn = User.TABLE, entityColumn = DataNotation.PK) public User user;

}
