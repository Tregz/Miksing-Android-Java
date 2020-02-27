package com.tregz.miksing.data;

import io.reactivex.Single;

public class DataUpdate extends DataSingle<Integer> {

    public DataUpdate(Single<Integer> single) {
        subscribe(single);
    }
}
