package com.tregz.miksing.data.user;

import android.content.Context;

import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataSingle;

public class UserDelete extends DataSingle<Integer> {

    public UserDelete(Context context) {
        super(context);
    }

    public void wipe() {
        subscribe(DataReference.getInstance(context).accessUser().wipe());
    }
}
