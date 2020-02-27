package com.tregz.miksing.data.user.tube;

import com.tregz.miksing.data.DataSingle;

import java.util.List;

import io.reactivex.Single;

class UserTubeInsert extends DataSingle<List<Long>> {

    UserTubeInsert(Single<List<Long>> single) {
        subscribe(single);
    }
}
