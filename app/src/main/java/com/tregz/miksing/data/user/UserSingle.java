package com.tregz.miksing.data.user;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;
import com.tregz.miksing.data.song.SongAccess;

abstract class UserSingle<T> extends DataSingle<T> {

    UserSingle(Context context) {
        super(context);
    }

    UserAccess access() {
        return DataReference.getInstance(context).accessUser();
    }
}
