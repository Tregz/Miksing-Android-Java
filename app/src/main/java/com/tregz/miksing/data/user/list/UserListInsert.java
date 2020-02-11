package com.tregz.miksing.data.user.list;

import android.content.Context;

import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.data.song.SongSingle;
import com.tregz.miksing.data.user.list.song.ListSong;
import com.tregz.miksing.data.user.list.song.ListSongInsert;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

class UserListInsert extends UserListSingle<List<Long>> {

    UserListInsert(Context context, final UserList...data) {
        super(context);
        access().insert(data).subscribeOn(Schedulers.io()).subscribe(this);
    }
}
