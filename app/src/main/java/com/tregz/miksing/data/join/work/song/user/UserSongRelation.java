package com.tregz.miksing.data.join.work.song.user;

import androidx.room.Embedded;

import com.tregz.miksing.data.item.work.song.Song;

public class UserSongRelation {

    @Embedded public Song song;
    @Embedded public UserSong join;

}
